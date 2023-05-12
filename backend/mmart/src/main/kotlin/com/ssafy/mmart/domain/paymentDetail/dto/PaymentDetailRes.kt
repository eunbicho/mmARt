package com.ssafy.mmart.domain.paymentDetail.dto

import com.ssafy.mmart.domain.item.dto.GetItemRes

data class PaymentDetailRes (
    val paymentDetailIdx: Int,
    val quantity: Int,
    val discount: Int,
    val totalPrice: Int,
    val item: GetItemRes,
    val isWriteReview:Boolean,
) {
}