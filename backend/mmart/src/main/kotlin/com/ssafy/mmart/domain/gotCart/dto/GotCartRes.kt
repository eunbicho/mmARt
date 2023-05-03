package com.ssafy.mmart.domain.gotCart.dto

data class GotCartRes (
    var itemList: MutableList<GotCartItem>,
    var total: Int,
){
}