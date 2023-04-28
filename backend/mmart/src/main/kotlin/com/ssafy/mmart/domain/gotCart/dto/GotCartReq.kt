package com.ssafy.mmart.domain.gotCart.dto

data class GotCartReq(
    val userIdx: Int,
    val itemIdx: Int,
    val inventory: Int = 1,
) {
}