package com.ssafy.mmart.domain.gotCart.dto

data class GotCartItem (
    val itemIdx:Int,
    val itemName:String,
    val price:Int,
    val thumbnail:String,
    val isCoupon:Boolean,
    val couponPrice:Int,
    val quantity:Int,
)