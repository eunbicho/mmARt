package com.ssafy.mmart.domain.itemCoupon

import com.ssafy.mmart.domain.Base
import java.time.LocalDateTime
import javax.persistence.*

@Entity
data class ItemCoupon (
    @Id
    @Column(name = "itemCouponIdx")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val itemCouponIdx : Int? = null,

    @Column(name = "couponName")
    val couponName: String,

    @Column(name = "couponCost")
    val couponCost: Int,

    @Column(name = "couponExpired")
    val couponExpired: LocalDateTime,
): Base()
