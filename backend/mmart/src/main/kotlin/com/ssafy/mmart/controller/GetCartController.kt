package com.ssafy.mmart.controller

import com.ssafy.mmart.domain.ResultResponse
import com.ssafy.mmart.domain.getCart.dto.CreateGetCartReq
import com.ssafy.mmart.domain.getCart.dto.GetCartRes
import com.ssafy.mmart.service.GetCartService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("api/v1/getcarts")
class GetCartController @Autowired constructor(
    val getCartService: GetCartService,
) {
    @PostMapping
    fun createGetCart(@RequestBody createGetCartReq: CreateGetCartReq): ResultResponse<GetCartRes> {
        return ResultResponse.success(getCartService.createGetCart(createGetCartReq))
    }

    @GetMapping("{userIdx}")
    fun getGetCart(@PathVariable userIdx:Int): ResultResponse<GetCartRes> {
        return ResultResponse.success(getCartService.getGetCart(userIdx))
    }

    @DeleteMapping("{userIdx}")//장볼구니 비우기
    fun deleteGetCarts(@PathVariable userIdx:Int): ResultResponse<GetCartRes> {
        return ResultResponse.success(getCartService.deleteGetCarts(userIdx))
    }

}