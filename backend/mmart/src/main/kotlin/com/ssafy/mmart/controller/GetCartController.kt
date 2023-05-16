package com.ssafy.mmart.controller

import com.ssafy.mmart.domain.ResultResponse
import com.ssafy.mmart.domain.getCart.dto.CreateGetCartReq
import com.ssafy.mmart.domain.getCart.dto.GetCartPathRes
import com.ssafy.mmart.domain.getCart.dto.GetCartRes
import com.ssafy.mmart.domain.getCart.dto.PutGetCartReq
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

    @PutMapping
    fun putGetCart(@RequestBody putGetCartReq: PutGetCartReq): ResultResponse<GetCartRes> {
        return ResultResponse.success(getCartService.putGetCart(putGetCartReq))
    }

    @GetMapping("/{userIdx}")
    fun getGetCart(@PathVariable userIdx: Int): ResultResponse<GetCartRes> {
        return ResultResponse.success(getCartService.getGetCart(userIdx))
    }

    @GetMapping("/shortest-path/{userIdx}")
    fun getShortestPathGetCart(
        @PathVariable userIdx: Int,
        @RequestParam startNode: String
    ): ResultResponse<GetCartPathRes?> {
        return ResultResponse.success(getCartService.getShortestPathGetCart(userIdx,startNode))
    }

    @DeleteMapping("/{userIdx}")//장볼구니 비우기
    fun deleteGetCarts(@PathVariable userIdx: Int): ResultResponse<GetCartRes> {
        return ResultResponse.success(getCartService.deleteGetCarts(userIdx))
    }

    @DeleteMapping//장볼구니 아이템 삭제
    fun deleteGetCart(@RequestParam userIdx: Int, itemIdx: Int): ResultResponse<GetCartRes> {
        return ResultResponse.success(getCartService.deleteGetCart(userIdx, itemIdx))
    }

}