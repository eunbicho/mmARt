package com.ssafy.mmart.controller

import com.ssafy.mmart.domain.ResultResponse
import com.ssafy.mmart.domain.itemCoupon.ItemCoupon
import com.ssafy.mmart.service.ItemCouponService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("api/v1/coupons")
class ItemCouponController @Autowired constructor(
    val itemCouponService: ItemCouponService,
) {
    @GetMapping("/{itemIdx}")
    fun getCoupon(@PathVariable itemIdx:Int): ResultResponse<ItemCoupon?> {
        return ResultResponse.success(itemCouponService.getCoupon(itemIdx))
    }

}