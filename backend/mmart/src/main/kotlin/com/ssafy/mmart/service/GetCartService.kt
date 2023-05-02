package com.ssafy.mmart.service

import com.ssafy.mmart.domain.getCart.dto.CreateGetCartReq
import com.ssafy.mmart.domain.getCart.dto.GetCartItem
import com.ssafy.mmart.domain.getCart.dto.GetCartRes
import com.ssafy.mmart.domain.getCart.dto.PutGetCartReq
import com.ssafy.mmart.exception.bad_request.BadAccessException
import com.ssafy.mmart.exception.conflict.GetCartEmptyException
import com.ssafy.mmart.exception.not_found.*
import com.ssafy.mmart.repository.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.redis.core.HashOperations
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service

@Service
class GetCartService @Autowired constructor(
    var redisTemplate: RedisTemplate<String, Any>,
    var userRepository: UserRepository,
    var itemRepository: ItemRepository,
    var categoryRepository: CategoryRepository,
    var itemItemCouponRepository: ItemItemCouponRepository,
    var couponRepository: ItemCouponRepository,
    ) {
    //카테고리는 인덱스를 마이너스로, 인벤토리는 0으로 쓰기
    val GETCART = "GETCART"
    val getCartOps: HashOperations<String, Int, MutableMap<Int, Int>> = redisTemplate.opsForHash()

    fun createGetCart(createGetCartReq: CreateGetCartReq): GetCartRes {
        //유저가 존재하는지 확인
        userRepository.findById(createGetCartReq.userIdx).orElseThrow(::UserNotFoundException)
        var temp = getCartOps.get(GETCART, createGetCartReq.userIdx)
        if (createGetCartReq.itemIdx < 0) {
            //카테고리 추가일 경우
            //카테고리가 존재하는지 확인해야함!!
            categoryRepository.findById(-createGetCartReq.itemIdx).orElseThrow(::ItemNotFoundException)

            if (temp == null) {
                var map: MutableMap<Int, Int> = mutableMapOf()
                map.put(createGetCartReq.itemIdx, 0)
                getCartOps.put(GETCART, createGetCartReq.userIdx, map)
            } else {
                //MAP에 내가 넣으려는 값이 있는지 체크
                val flag = temp.containsKey(createGetCartReq.itemIdx)
                if (!flag) {//값이 없으면
                    temp.put(createGetCartReq.itemIdx, 0)
                }
                getCartOps.put(GETCART, createGetCartReq.userIdx, temp)
            }
        } else {
            //아이템 추가일 경우
            //아이템 존재하는지 확인
            itemRepository.findById(createGetCartReq.itemIdx).orElseThrow(::ItemNotFoundException)
            if (temp == null) {
                var map: MutableMap<Int, Int> = mutableMapOf()
                map.put(createGetCartReq.itemIdx, 1)
                getCartOps.put(GETCART, createGetCartReq.userIdx, map)
            } else {
                //MAP에 내가 넣으려는 값이 있는지 체크
                val flag = temp.containsKey(createGetCartReq.itemIdx)
                if (flag) {//값이 있으면
                    temp.put(createGetCartReq.itemIdx, temp.get(createGetCartReq.itemIdx)!! + 1)
                } else {//값이 없으면
                    temp.put(createGetCartReq.itemIdx, 1)
                }
                getCartOps.put(GETCART, createGetCartReq.userIdx, temp)
            }
        }
//        getCartOps.entries(GETCART).keys.forEach { haskKey -> getCartOps.delete(GETCART, haskKey) }
        return setGetCarts(temp!!)
    }

    fun putGetCart(putGetCartReq: PutGetCartReq): GetCartRes {
        //유저가 존재하는지 확인
        if (putGetCartReq.itemIdx < 0)
            throw BadAccessException()
        userRepository.findById(putGetCartReq.userIdx).orElseThrow(::UserNotFoundException)
        //아이템 존재하는지 확인
        var item = itemRepository.findById(putGetCartReq.itemIdx).orElseThrow(::ItemNotFoundException)

        //선택한 수량이 0 초과인지 체크
        if (putGetCartReq.inventory <= 0)
            throw WrongQuantityException()

        //내가 담기를 원하는 재고가 기존의 수량을 넘는지 체크
        if (item.inventory < putGetCartReq.inventory)
            throw OverQuantityException()

        var temp = getCartOps.get(GETCART, putGetCartReq.userIdx)
        if (temp.isNullOrEmpty()) {
            throw GetCartEmptyException()
        } else {
            //MAP에 내가 넣으려는 값이 있는지 체크
            val flag = temp.containsKey(putGetCartReq.itemIdx)
            if (flag) {//값이 있으면(있어야함. 수량 수정이므로)
                temp.put(putGetCartReq.itemIdx, putGetCartReq.inventory)
            } else {//값이 없으면 익셉션 발생
                throw GetCartNotFoundException()
            }
            getCartOps.put(GETCART, putGetCartReq.userIdx, temp)
        }
        return setGetCarts(temp!!)
    }

    open fun getGetCart(userIdx: Int): GetCartRes {
        //유저가 존재하는지 확인
        userRepository.findById(userIdx).orElseThrow(::UserNotFoundException)

        var temp = getCartOps.get(GETCART, userIdx)
        if (temp.isNullOrEmpty()) {
            throw GetCartEmptyException()
        } else {
            return setGetCarts(temp!!)
        }
    }

    fun deleteGetCarts(userIdx: Int): GetCartRes {
        //유저가 존재하는지 확인
        userRepository.findById(userIdx).orElseThrow(::UserNotFoundException)
        var temp = getCartOps.get(GETCART, userIdx)
        if (!temp.isNullOrEmpty()) {
            temp.clear()
            getCartOps.put(GETCART, userIdx, temp)
        }
        return setGetCarts(temp!!)
    }

    fun deleteGetCart(userIdx: Int, itemIdx: Int): GetCartRes {
        //유저가 존재하는지 확인
        userRepository.findById(userIdx).orElseThrow(::UserNotFoundException)
        var temp = getCartOps.get(GETCART, userIdx)
        if (temp.isNullOrEmpty())
            throw GetCartEmptyException()
        if (itemIdx < 0) {
            //카테고리인 경우
            //카테고리가 존재하는지 확인
            categoryRepository.findById(-itemIdx).orElseThrow(::ItemNotFoundException)

        } else {
            //아이템인 경우
            //아이템 존재하는지 확인
            itemRepository.findById(itemIdx).orElseThrow(::ItemNotFoundException)
        }
        //MAP에 내가 삭제하려는 값이 있는지 체크
        val flag = temp.containsKey(itemIdx)
        if (flag) {//값이 있으면(있어야함. 삭제이므로)
            temp.remove(itemIdx)
        } else {//값이 없으면 익셉션 발생
            throw GetCartNotFoundException()
        }
        getCartOps.put(GETCART, userIdx, temp)

        return setGetCarts(temp!!)
    }

    fun setGetCarts(temp: MutableMap<Int, Int>): GetCartRes{
        var getCartRes = GetCartRes(mutableListOf(),0)
        temp!!.keys.forEach { haskKey -> getCartRes.itemList.add(GetCartItem(haskKey, temp.get(haskKey)!!))
            var item = itemRepository.findById(haskKey).orElseThrow(::ItemNotFoundException)
            var eachPrice = item.price
            //쿠폰이 있으면, 쿠폰 가격만큼 item의 price에서 빼준다.
            var itemItemCoupon = itemItemCouponRepository.findByItem_ItemIdx(item.itemIdx!!)
            if(itemItemCoupon != null){
                var itemCoupon = couponRepository.findById(itemItemCoupon.itemCoupon.itemCouponIdx!!)
                eachPrice-=itemCoupon.get().couponDiscount
            }
            getCartRes.total+=eachPrice*temp.get(haskKey)!!}
        return getCartRes

    }

}
