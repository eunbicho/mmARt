package com.ssafy.mmart.domain.gotCart.dto

data class GotCartRes (
    var itemList: MutableList<GotCartItem>,
    var priceTotal: Int,
    var discountTotal: Int,
    var total: Int,
)