package com.ssafy.mmart.domain.getCart.dto

data class PutGetCartReq(
    val userIdx:Int,
    val itemIdx:Int,
    val inventory:Int,
) {
}