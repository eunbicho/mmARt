package com.ssafy.mmart.domain.review.dto

data class ReviewReq(
    var content: String,
    var star: Int,
    var itemIdx: Int,
    var userIdx: Int,
) {
}