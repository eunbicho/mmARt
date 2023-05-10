package com.ssafy.mmart.service

import com.querydsl.jpa.impl.JPAQueryFactory
import com.ssafy.mmart.domain.gotCart.dto.GotCartItem
import com.ssafy.mmart.domain.gotCart.dto.GotCartReq
import com.ssafy.mmart.domain.gotCart.dto.GotCartRes
import com.ssafy.mmart.domain.gotCart.dto.GotCartToPayRes
import com.ssafy.mmart.domain.item.QItem
import com.ssafy.mmart.domain.itemCoupon.QItemCoupon
import com.ssafy.mmart.domain.itemItemCoupon.QItemItemCoupon
import com.ssafy.mmart.exception.conflict.GotCartEmptyException
import com.ssafy.mmart.exception.not_found.*
import com.ssafy.mmart.repository.ItemCouponRepository
import com.ssafy.mmart.repository.ItemItemCouponRepository
import com.ssafy.mmart.repository.ItemRepository
import com.ssafy.mmart.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.redis.core.HashOperations
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class GotCartService @Autowired constructor(
    private var redisTemplate: RedisTemplate<String, Any>,
    val userRepository: UserRepository,
    val itemRepository: ItemRepository,
    val itemCouponRepository: ItemCouponRepository,
    val itemItemCouponRepository: ItemItemCouponRepository,
    private val jpaQueryFactory: JPAQueryFactory,
){
    val gotCart = "GOTCART"
    val gotCartOps: HashOperations<String, Int, MutableMap<Int, Int>> = redisTemplate.opsForHash()


    fun setGotCarts(temp: MutableMap<Int, Int>): GotCartRes {
        var total = 0
        var priceTotal = 0
        var discountTotal = 0
        val gotCartRes = GotCartRes(mutableListOf(), priceTotal, discountTotal, total)
        temp.keys.forEach{ hashKey ->
            val tempQuantity = temp[hashKey]!!
            val tempItem = itemRepository.findByIdOrNull(hashKey) ?: throw ItemNotFoundException()
            var tempPrice = tempItem.price
            var isCoupon = false
            val tempCoupon = jpaQueryFactory
                .selectFrom(QItemItemCoupon.itemItemCoupon)
                .join(QItemItemCoupon.itemItemCoupon.item, QItem.item)
                .join(QItemItemCoupon.itemItemCoupon.itemCoupon, QItemCoupon.itemCoupon)
                .where(
                    QItemItemCoupon.itemItemCoupon.item.eq(tempItem), QItemCoupon.itemCoupon.couponExpired.after(
                        LocalDateTime.now()
                    ))
                .orderBy(QItemCoupon.itemCoupon.couponDiscount.desc())
                .fetchOne()
            if (tempCoupon != null) {
                isCoupon=true
                tempPrice -= itemCouponRepository.findByIdOrNull(tempCoupon.itemCoupon.itemCouponIdx)!!.couponDiscount
            }
            gotCartRes.itemList.add(
                GotCartItem(
                    hashKey,
                    tempItem.itemName,
                    tempItem.price,
                    tempItem.thumbnail!!,
                    isCoupon,
                    tempPrice,
                    tempQuantity
                )
            )
            priceTotal += tempItem.price * tempQuantity
            discountTotal += discountTotal * tempQuantity
            total += tempPrice * tempQuantity
        }
        gotCartRes.priceTotal = priceTotal
        gotCartRes.discountTotal = priceTotal - total
        gotCartRes.total = total
        return gotCartRes
    }

    fun getGotCarts(userIdx: Int): GotCartRes {
        userRepository.findByIdOrNull(userIdx) ?: throw UserNotFoundException()
        val temp = gotCartOps.get(gotCart, userIdx) ?: mutableMapOf()
        return setGotCarts(temp)
    }

    fun getGotCartsByEmail(email: String): GotCartToPayRes {
        val user = userRepository.findByEmail(email) ?: throw UserNotFoundException()
        val temp = gotCartOps.get(gotCart, user.userIdx!!) ?: mutableMapOf()
        return GotCartToPayRes(setGotCarts(temp), user.userIdx)
    }

    fun createGotCart(gotCartReq: GotCartReq): GotCartRes {
        userRepository.findByIdOrNull(gotCartReq.userIdx) ?: throw UserNotFoundException()
        itemRepository.findByIdOrNull(gotCartReq.itemIdx) ?: throw ItemNotFoundException()

        var temp = gotCartOps.get(gotCart, gotCartReq.userIdx)
        if (temp.isNullOrEmpty()) {
            temp = mutableMapOf()
            temp[gotCartReq.itemIdx] = gotCartReq.quantity
            gotCartOps.put(gotCart, gotCartReq.userIdx, temp)
        } else {
            if (temp.containsKey(gotCartReq.itemIdx)) {
                temp[gotCartReq.itemIdx] = temp[gotCartReq.itemIdx]!! + gotCartReq.quantity
            } else {
                temp[gotCartReq.itemIdx] = gotCartReq.quantity
            }
            gotCartOps.put(gotCart, gotCartReq.userIdx, temp)
        }
        return setGotCarts(temp)
    }

    fun updateGotCart(gotCartReq: GotCartReq): GotCartRes {
        userRepository.findByIdOrNull(gotCartReq.userIdx) ?: throw UserNotFoundException()
        val item = itemRepository.findByIdOrNull(gotCartReq.itemIdx) ?: throw ItemNotFoundException()

        if (gotCartReq.quantity <= 0 ) throw WrongQuantityException()
        if (gotCartReq.quantity > item.inventory) throw OverQuantityException()

        val temp = gotCartOps.get(gotCart, gotCartReq.userIdx)
        if (temp.isNullOrEmpty()) {
            throw GotCartEmptyException()
        } else {
            if (temp.containsKey(gotCartReq.itemIdx)) {
                temp[gotCartReq.itemIdx] = gotCartReq.quantity
            } else {
                throw GotCartNotFoundException()
            }
            gotCartOps.put(gotCart, gotCartReq.userIdx, temp)
        }
        return setGotCarts(temp)
    }

    fun deleteGotCarts(userIdx: Int): GotCartRes {
        userRepository.findByIdOrNull(userIdx) ?: throw UserNotFoundException()
        val temp = gotCartOps.get(gotCart, userIdx)
        if (!temp.isNullOrEmpty()){
            temp.clear()
//            gotCartOps.put(gotCart, userIdx, temp)
            gotCartOps.delete(gotCart, userIdx)
        } else throw GotCartNotFoundException()
        return setGotCarts(temp)
    }

    fun deleteGotCart(userIdx: Int, itemIdx: Int): GotCartRes {
        userRepository.findByIdOrNull(userIdx) ?: throw UserNotFoundException()
        itemRepository.findByIdOrNull(itemIdx) ?: throw ItemNotFoundException()
        val temp = gotCartOps.get(gotCart, userIdx)
        if (temp.isNullOrEmpty()) throw GotCartEmptyException()
        if (temp.containsKey(itemIdx)) {
            temp.remove(itemIdx)
        } else {
            throw GotCartNotFoundException()
        }
        gotCartOps.put(gotCart, userIdx, temp)
        return setGotCarts(temp)
    }
}