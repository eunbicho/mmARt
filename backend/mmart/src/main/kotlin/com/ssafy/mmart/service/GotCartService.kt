package com.ssafy.mmart.service

import com.ssafy.mmart.domain.gotCart.dto.GotCartItem
import com.ssafy.mmart.domain.gotCart.dto.GotCartReq
import com.ssafy.mmart.domain.gotCart.dto.GotCartRes
import com.ssafy.mmart.exception.not_found.ItemNotFoundException
import com.ssafy.mmart.exception.not_found.UserNotFoundException
import com.ssafy.mmart.repository.ItemRepository
import com.ssafy.mmart.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.redis.core.HashOperations
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class GotCartService @Autowired constructor(
    var redisTemplate: RedisTemplate<String, Any>,
    val userRepository: UserRepository,
    private val itemRepository: ItemRepository,
){
    val GOTCART = "GOTCART"
    val gotCartOps: HashOperations<String, Int, MutableMap<Int, Int>> = redisTemplate.opsForHash()

    fun getGotCart(userIdx: Int): GotCartRes {
        userRepository.findByIdOrNull(userIdx) ?: throw UserNotFoundException()

        var temp = gotCartOps.get(GOTCART, userIdx)
        var gotCartRes: GotCartRes = GotCartRes(mutableListOf())
        temp!!.keys.forEach{ hashKey -> gotCartRes.itemList.add(GotCartItem(hashKey, temp[hashKey]!!))}
        return gotCartRes
    }

    fun createGotCart(gotCartReq: GotCartReq): GotCartRes {
        userRepository.findByIdOrNull(gotCartReq.userIdx) ?: throw UserNotFoundException()
        itemRepository.findByIdOrNull(gotCartReq.itemIdx) ?: throw ItemNotFoundException()

        var temp = gotCartOps.get(GOTCART, gotCartReq.userIdx)
        if (temp.isNullOrEmpty()) {
            var map: MutableMap<Int, Int> = mutableMapOf()
            map[gotCartReq.itemIdx] = 1
            gotCartOps.put(GOTCART, gotCartReq.userIdx, map)
        } else {
            if (temp.containsKey(gotCartReq.itemIdx)) {
                temp[gotCartReq.itemIdx] = temp[gotCartReq.itemIdx]!!+1
            } else {
                temp[gotCartReq.itemIdx] = 1
            }
            gotCartOps.put(GOTCART, gotCartReq.userIdx, temp)
        }
        var gotCartRes: GotCartRes = GotCartRes(mutableListOf())
        temp!!.keys.forEach{ hashKey -> gotCartRes.itemList.add(GotCartItem(hashKey, temp[hashKey]!!)) }

        return gotCartRes
    }
}