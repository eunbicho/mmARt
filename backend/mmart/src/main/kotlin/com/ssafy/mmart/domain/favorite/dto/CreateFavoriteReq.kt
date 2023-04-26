package com.ssafy.mmart.domain.favorite.dto

data class CreateFavoriteReq(
    var categoryIdx:Int,
    var itemIdx: Int,
    var userIdx: Int,
    )