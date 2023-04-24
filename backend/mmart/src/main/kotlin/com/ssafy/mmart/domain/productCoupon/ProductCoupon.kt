package com.ssafy.mmart.domain.productCoupon

import com.ssafy.mmart.domain.Base
import java.time.LocalDateTime
import javax.persistence.*

@Entity
data class ProductCoupon (
    @Id
    @Column(name = "productCouponIdx")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var productCouponIdx : Int? = null,

    @Column(name = "couponName")
    var couponName: String,

    @Column(name = "couponCost")
    var couponCost: Int,

    @Column(name = "couponExpired")
    var couponExpired: LocalDateTime,
): Base()
