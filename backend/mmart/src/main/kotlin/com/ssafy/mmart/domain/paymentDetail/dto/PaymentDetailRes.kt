package com.ssafy.mmart.domain.paymentDetail.dto

import com.ssafy.mmart.domain.item.Item
import com.ssafy.mmart.domain.item.dto.GetItemRes
import com.ssafy.mmart.domain.payment.Payment
import com.ssafy.mmart.domain.paymentDetail.PaymentDetail
import com.ssafy.mmart.domain.review.Review
import com.ssafy.mmart.domain.user.User
import com.ssafy.mmart.domain.user.dto.UserRes
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import javax.persistence.*

data class PaytmentDetailRes (
    val paymentDetailIdx: Int,
    val quantity: Int,
    val discount: Int,
    val totalPrice: Int,
    val itemRes: GetItemRes,
) {
}