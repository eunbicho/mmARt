package com.ssafy.mmart.domain.getCart.dto

data class GetCartItem(
    val itemIdx:Int,
    val itemName:String,
    val price:Int,
    val thumbnail:String,
    val isCoupon:Boolean,
    val couponPrice:Int,
    val quantity:Int,
)