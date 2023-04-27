package com.ssafy.mmart.domain.getCart.dto

data class GetCartRes(
    var itemList: MutableList<GetCartItem>,
) {
}