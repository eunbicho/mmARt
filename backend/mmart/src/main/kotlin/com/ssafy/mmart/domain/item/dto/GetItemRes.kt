package com.ssafy.mmart.domain.item.dto

import com.ssafy.mmart.domain.item.Item
import com.ssafy.mmart.domain.itemDetail.ItemDetail

data class GetItemRes(
    var itemIdx: Int?,
    var itemName: String,
    var price: Int,
    val isCoupon:Boolean,
    val couponPrice:Int,
    var inventory: Int,
    var barcode: String,
    var thumbnail: String? = "",
    var placeInfo: String,
    var weight: Int,
    var content: String?,
)
//{
//    fun toEntity(item: Item, itemDetail: ItemDetail): GetItemRes = GetItemRes(
//        itemIdx = item.itemIdx,
//        itemName = item.itemName,
//        price = item.price,
//        inventory = item.inventory,
//        barcode = item.barcode,
//        thumbnail = item.thumbnail,
//        placeInfo = item.placeInfo,
//        weight = item.weight,
//        content = itemDetail.content,
//    )
//}