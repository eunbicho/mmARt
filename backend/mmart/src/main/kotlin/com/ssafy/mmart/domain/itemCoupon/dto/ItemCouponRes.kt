package com.ssafy.mmart.domain.itemCoupon.dto

import java.time.LocalDateTime
import javax.persistence.Id

data class ItemCouponRes(
    val itemCouponIdx: Int,
    val couponName: String,
    val couponDiscount: Int,
    val couponExpired: LocalDateTime,
) {

}