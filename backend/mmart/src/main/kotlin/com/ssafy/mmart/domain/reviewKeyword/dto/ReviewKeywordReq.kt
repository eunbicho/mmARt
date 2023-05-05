package com.ssafy.mmart.domain.reviewKeyword.dto

data class ReviewKeywordReq(
    var isPositive: Boolean = true,
    var keywordContent: String,
    var reviewIdx: Int,
) {
}