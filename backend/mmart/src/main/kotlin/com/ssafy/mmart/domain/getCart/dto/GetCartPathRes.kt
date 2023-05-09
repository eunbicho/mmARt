package com.ssafy.mmart.domain.getCart.dto

data class GetCartPathRes(
    var itemList: MutableList<SortItemRes>,
    var totalPath:List<String>,
)