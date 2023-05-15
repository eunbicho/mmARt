package com.ssafy.mmart.domain.getCart.dto

data class GetCartItem(
    val itemIdx:Int,
    val itemName:String,
    val price:Int,
    val thumbnail:String,
    val isCoupon:Boolean,
    val couponPrice:Int,
    val placeInfo: String,
    val quantity:Int,
    //추가해줘야 하는 컬럼
    val inventory: Int,
    val barcode: String,
    val weight: Int,
    val content: String?,
)