package com.ssafy.mmart.domain.review.dto

import com.ssafy.mmart.domain.item.dto.GetItemRes
import com.ssafy.mmart.domain.user.dto.UserRes

data class ReviewRes(
    val reviewIdx: Int,
    var content: String,
    var star: Int,
    val user: UserRes,
    val item: GetItemRes,
    val date: String,
) {
}