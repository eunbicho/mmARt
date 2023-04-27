package com.ssafy.mmart.service

import com.ssafy.mmart.domain.getCart.dto.CreateGetCartReq
import com.ssafy.mmart.domain.getCart.dto.GetCartRes
import com.ssafy.mmart.domain.getCart.dto.GetCartItem
import com.ssafy.mmart.exception.conflict.GetCartEmptyException
import com.ssafy.mmart.exception.not_found.*
import com.ssafy.mmart.repository.ItemRepository
import com.ssafy.mmart.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.redis.core.HashOperations
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service

@Service
class GetCartService @Autowired constructor(
    var redisTemplate: RedisTemplate<String, Any>,
    var userRepository: UserRepository,
    var itemRepository: ItemRepository,

) {
    val GETCART = "GETCART"
    val getCartOps: HashOperations<String, Int, MutableMap<Int,Int>> = redisTemplate.opsForHash()

    fun createGetCart(createGetCartReq: CreateGetCartReq): GetCartRes {
        //유저가 존재하는지 확인
        userRepository.findById(createGetCartReq.userIdx).orElseThrow(::UserNotFoundException)
        //아이템 존재하는지 확인
        itemRepository.findById(createGetCartReq.itemIdx).orElseThrow(::ItemNotFoundException)

        var temp = getCartOps.get(GETCART,createGetCartReq.userIdx)
        if(temp==null){
            var map :MutableMap<Int,Int> = mutableMapOf()
            map.put(createGetCartReq.itemIdx,1)
            getCartOps.put(GETCART,createGetCartReq.userIdx,map)
        }else{
            //MAP에 내가 넣으려는 값이 있는지 체크
            val flag = temp.containsKey(createGetCartReq.itemIdx)
            if(flag){//값이 있으면
                temp.put(createGetCartReq.itemIdx,temp.get(createGetCartReq.itemIdx)!!+1)
            }else{//값이 없으면
                temp.put(createGetCartReq.itemIdx,1)
            }
            getCartOps.put(GETCART,createGetCartReq.userIdx,temp)
        }
//        getCartOps.entries(GETCART).keys.forEach { haskKey -> getCartOps.delete(GETCART, haskKey) }
        var temp2 = getCartOps.get(GETCART,createGetCartReq.userIdx)
        var getCartRes:GetCartRes = GetCartRes(mutableListOf())
        temp2!!.keys.forEach{ haskKey -> getCartRes.itemList.add(GetCartItem(haskKey,temp2.get(haskKey)!!)) }
        return getCartRes
    }

    fun getGetCart(userIdx: Int): GetCartRes {
        //유저가 존재하는지 확인
        userRepository.findById(userIdx).orElseThrow(::UserNotFoundException)

        var temp = getCartOps.get(GETCART,userIdx)
        if(temp==null || temp.isEmpty()){
           throw GetCartEmptyException()
        }else{
            var getCartRes:GetCartRes = GetCartRes(mutableListOf())
            temp!!.keys.forEach{ haskKey -> getCartRes.itemList.add(GetCartItem(haskKey,temp.get(haskKey)!!)) }
            return getCartRes
        }
    }

    fun deleteGetCarts(userIdx: Int): GetCartRes {
        //유저가 존재하는지 확인
        userRepository.findById(userIdx).orElseThrow(::UserNotFoundException)
        var getCartRes:GetCartRes = GetCartRes(mutableListOf())
        var temp = getCartOps.get(GETCART,userIdx)
        if(!temp.isNullOrEmpty()){
            temp.clear()
            getCartOps.put(GETCART,userIdx,temp)
        }
        var temp2 = getCartOps.get(GETCART,userIdx)
        print(temp2.toString())
        return getCartRes
    }

}
