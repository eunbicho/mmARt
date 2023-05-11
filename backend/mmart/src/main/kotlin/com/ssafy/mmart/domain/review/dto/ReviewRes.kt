package com.ssafy.mmart.domain.review.dto

import com.ssafy.mmart.domain.item.Item
import com.ssafy.mmart.domain.paymentDetail.PaymentDetail
import com.ssafy.mmart.domain.review.Review
import com.ssafy.mmart.domain.user.User
import com.ssafy.mmart.domain.user.dto.UserRes

data class ReviewRes (
    val reviewIdx:Int,
    var content: String,
    var star: Int,
    val user : UserRes,
) {
}