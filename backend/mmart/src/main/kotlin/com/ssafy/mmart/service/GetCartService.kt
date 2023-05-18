package com.ssafy.mmart.service

import com.querydsl.jpa.impl.JPAQueryFactory
import com.ssafy.mmart.domain.getCart.dto.*
import com.ssafy.mmart.domain.item.QItem
import com.ssafy.mmart.domain.itemCoupon.QItemCoupon.itemCoupon
import com.ssafy.mmart.domain.itemItemCoupon.QItemItemCoupon.itemItemCoupon
import com.ssafy.mmart.exception.bad_request.BadAccessException
import com.ssafy.mmart.exception.conflict.GetCartEmptyException
import com.ssafy.mmart.exception.not_found.*
import com.ssafy.mmart.repository.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.redis.core.HashOperations
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service
import java.io.IOException
import java.time.LocalDateTime.now

@Service
class GetCartService @Autowired constructor(
    private var redisTemplate: RedisTemplate<String, Any>,
    val jpaQueryFactory: JPAQueryFactory,
    var userRepository: UserRepository,
    var itemRepository: ItemRepository,
    var categoryRepository: CategoryRepository,
    var itemItemCouponRepository: ItemItemCouponRepository,
    var couponRepository: ItemCouponRepository,
    var itemDetailRepository: ItemDetailRepository,
) {
    //카테고리는 인덱스를 마이너스로, 인벤토리는 0으로 쓰기
    val getCart = "GETCART"
    val getCartOps: HashOperations<String, Int, MutableMap<Int, Int>> = redisTemplate.opsForHash()

    fun createGetCart(createGetCartReq: CreateGetCartReq): GetCartRes {
        //유저가 존재하는지 확인
        userRepository.findById(createGetCartReq.userIdx).orElseThrow(::UserNotFoundException)
        var temp = getCartOps.get(getCart, createGetCartReq.userIdx)

        //선택한 수량이 0 초과인지 체크
        if (createGetCartReq.quantity <= 0)
            throw WrongQuantityException()

        //아이템 존재하는지 확인
        val item = itemRepository.findById(createGetCartReq.itemIdx).orElseThrow(::ItemNotFoundException)

        //내가 담기를 원하는 재고가 기존의 수량을 넘는지 체크
        if (item.inventory < createGetCartReq.quantity)
            throw OverQuantityException()

        if (temp == null) {
            temp = mutableMapOf()
            temp[createGetCartReq.itemIdx] = createGetCartReq.quantity
            getCartOps.put(getCart, createGetCartReq.userIdx, temp)
        } else {
            //MAP에 내가 넣으려는 값이 있는지 체크
            val flag = temp.containsKey(createGetCartReq.itemIdx)
            if (flag) {//값이 있으면
                temp[createGetCartReq.itemIdx] = temp[createGetCartReq.itemIdx]!! + createGetCartReq.quantity
            } else {//값이 없으면
                temp[createGetCartReq.itemIdx] = createGetCartReq.quantity
            }
            getCartOps.put(getCart, createGetCartReq.userIdx, temp)
        }
        return setGetCarts(temp)
    }

    fun putGetCart(putGetCartReq: PutGetCartReq): GetCartRes {
        //유저가 존재하는지 확인
        if (putGetCartReq.itemIdx < 0)
            throw BadAccessException()
        userRepository.findById(putGetCartReq.userIdx).orElseThrow(::UserNotFoundException)
        //아이템 존재하는지 확인
        val item = itemRepository.findById(putGetCartReq.itemIdx).orElseThrow(::ItemNotFoundException)

        //선택한 수량이 0 초과인지 체크
        if (putGetCartReq.quantity <= 0)
            throw WrongQuantityException()

        //내가 담기를 원하는 재고가 기존의 수량을 넘는지 체크
        if (item.inventory < putGetCartReq.quantity)
            throw OverQuantityException()

        val temp = getCartOps.get(getCart, putGetCartReq.userIdx)
        if (temp.isNullOrEmpty()) {
            throw GetCartEmptyException()
        } else {
            //MAP에 내가 넣으려는 값이 있는지 체크
            val flag = temp.containsKey(putGetCartReq.itemIdx)
            if (flag) {//값이 있으면(있어야함. 수량 수정이므로)
                temp[putGetCartReq.itemIdx] = putGetCartReq.quantity
            } else {//값이 없으면 익셉션 발생
                throw GetCartNotFoundException()
            }
            getCartOps.put(getCart, putGetCartReq.userIdx, temp)
        }
        return setGetCarts(temp)
    }

    fun getGetCart(userIdx: Int): GetCartRes {
        //유저가 존재하는지 확인
        userRepository.findById(userIdx).orElseThrow(::UserNotFoundException)
        val temp = getCartOps.get(getCart, userIdx) ?: mutableMapOf()
        return setGetCarts(temp)
    }

    fun getShortestPathGetCart(userIdx: Int,startNode: String): GetCartPathRes?{
        //유저가 존재하는지 확인
        userRepository.findById(userIdx).orElseThrow(::UserNotFoundException)
        val getCartRes = getGetCart(userIdx)
        val tempList = mutableListOf<String>()
        //startNode 넣기
        tempList.add(startNode)
        val sortedItem = mutableListOf<SortItemRes>()
        //placeInfo만 추출해서 넣기
        getCartRes.itemList.forEach { cartItem ->
            if(!tempList.contains(cartItem.placeInfo)){
                tempList.add(cartItem.placeInfo)
            }
        }
        println(tempList)
        //최단경로 가져오기
        val client = OkHttpClient()
        val body = """
{ "locations": $tempList}
""".toRequestBody("application/json".toMediaType())
        val request = Request.Builder()
            .url("http://k8a405.p.ssafy.io:8789/shortest-path")
            .post(body)
            .build()
        println(request)
        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw IOException("Unexpected code $response")
            val responseBody = response.body?.string()?.split(":")?.get(1)
            //전체 경로 리스트
            val totalList = responseBody?.substring(1, responseBody.length - 2)?.split(",")
            totalList?.forEach { node ->
                val nodeIdx = getCartRes.itemList.find { it.placeInfo == node }
                if (nodeIdx != null) {

                    sortedItem.add(SortItemRes(nodeIdx.itemName, nodeIdx.placeInfo, getCartRes.itemList.count { it.placeInfo == node }))
                }
            }
            return GetCartPathRes(itemList = sortedItem, totalPath = totalList!!)
        }
    }

    fun deleteGetCarts(userIdx: Int): GetCartRes {
        //유저가 존재하는지 확인
        userRepository.findById(userIdx).orElseThrow(::UserNotFoundException)
        val temp = getCartOps.get(getCart, userIdx)
        if (!temp.isNullOrEmpty()) {
            temp.clear()
            getCartOps.put(getCart, userIdx, temp)
        }
        return setGetCarts(temp!!)
    }

    fun deleteGetCart(userIdx: Int, itemIdx: Int): GetCartRes {
        //유저가 존재하는지 확인
        userRepository.findById(userIdx).orElseThrow(::UserNotFoundException)
        val temp = getCartOps.get(getCart, userIdx)
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
        getCartOps.put(getCart, userIdx, temp)

        return setGetCarts(temp)
    }

    fun setGetCarts(temp: MutableMap<Int, Int>): GetCartRes {
        val getCartRes = GetCartRes(mutableListOf(), 0, 0, 0)
        if (temp.isNotEmpty()) {
            temp.keys.forEach { hashKey ->
                val item = itemRepository.findById(hashKey).orElseThrow(::ItemNotFoundException)
                var eachPrice = item.price
                var isCoupon = false
                //쿠폰이 있으면, 쿠폰 가격만큼 item의 price에서 빼준다.
                val itemItemCoupon = jpaQueryFactory
                    .selectFrom(itemItemCoupon)
                    .join(itemItemCoupon.item, QItem.item)
                    .join(itemItemCoupon.itemCoupon, itemCoupon)
                    .where(itemItemCoupon.item.eq(item), itemCoupon.couponExpired.after(now()))
                    .orderBy(itemCoupon.couponDiscount.desc())
                    .fetchOne()
                if (itemItemCoupon != null) {
                    isCoupon = true
                    val itemCoupon = couponRepository.findById(itemItemCoupon.itemCoupon.itemCouponIdx!!)
                    eachPrice -= itemCoupon.get().couponDiscount

                }
                getCartRes.itemList.add(
                    GetCartItem(
                        hashKey,
                        item.itemName,
                        item.price,
                        item.thumbnail!!,
                        isCoupon,
                        eachPrice,
                        item.placeInfo,
                        temp[hashKey]!!,
                        item.inventory,
                        item.barcode,
                        item.weight,
                        itemDetailRepository.findByItem(item)!!.content
                    )
                )
                getCartRes.priceTotal += item.price * temp[hashKey]!!
                getCartRes.total += eachPrice * temp[hashKey]!!
                getCartRes.discountTotal = getCartRes.priceTotal - getCartRes.total
            }
        }
        return getCartRes

    }

}
