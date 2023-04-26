package com.ssafy.mmart.domain.itemCoupon

import com.ssafy.mmart.domain.Base
import java.time.LocalDateTime
import javax.persistence.*

@Entity
data class ItemCoupon (
    @Id
    @Column(name = "itemCouponIdx")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var itemCouponIdx : Int? = null,

    @Column(name = "couponName")
    var couponName: String,

    @Column(name = "couponCost")
    var couponCost: Int,

    @Column(name = "couponExpired")
    var couponExpired: LocalDateTime,
): Base()
