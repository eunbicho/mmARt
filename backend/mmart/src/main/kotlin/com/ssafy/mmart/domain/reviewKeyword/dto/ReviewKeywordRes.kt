package com.ssafy.mmart.domain.reviewKeyword.dto

data class ReviewKeywordRes(
    var isPositive: Boolean = true,
    var keywordContent: String,
    var cnt:Int,
) {
}