package com.ssafy.mmart.domain.review.dto

import com.ssafy.mmart.domain.item.Item
import com.ssafy.mmart.domain.paymentDetail.PaymentDetail
import com.ssafy.mmart.domain.review.Review
import com.ssafy.mmart.domain.user.User

data class ReviewReq (
    var content: String,
    var star: Int,
) {
    fun toEntity(item: Item, paymentDetail: PaymentDetail, user: User, isPositive: Boolean): Review = Review(
        content = content,
        star = star,
        item = item,
        paymentDetail = paymentDetail,
        user = user,
        isPositive = isPositive
    )
}