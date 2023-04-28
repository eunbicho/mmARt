package com.ssafy.mmart.controller

import com.ssafy.mmart.domain.ResultResponse
import com.ssafy.mmart.domain.gotCart.dto.GotCartReq
import com.ssafy.mmart.domain.gotCart.dto.GotCartRes
import com.ssafy.mmart.service.GotCartService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("api/v1/gotcarts")
class GotCartController @Autowired constructor(
    val gotCartService: GotCartService,
){
    @GetMapping
    fun getGotCarts(@RequestParam userIdx: Int): ResultResponse<GotCartRes> {
        return ResultResponse.success(gotCartService.getGotCarts(userIdx))
    }

    @PostMapping
    fun createGotCart(@RequestBody gotCartReq: GotCartReq): ResultResponse<GotCartRes> {
        return ResultResponse.success(gotCartService.createGotCart(gotCartReq))
    }

    @PutMapping
    fun updateGotCart(@RequestBody gotCartReq: GotCartReq): ResultResponse<GotCartRes> {
        return ResultResponse.success(gotCartService.updateGotCart(gotCartReq))
    }

//    @DeleteMapping
//    fun deleteGotCarts(@RequestParam userIdx: Int): ResultResponse<GotCartRes> {
//        return ResultResponse.success(gotCartService.deleteGotCarts(userIdx))
//    }

    @DeleteMapping
    fun deleteGotCart(@RequestParam userIdx: Int, @RequestParam itemIdx: Int): ResultResponse<GotCartRes> {
        return ResultResponse.success(gotCartService.deleteGotCart(userIdx, itemIdx))
    }
}