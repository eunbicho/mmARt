package com.ssafy.mmart.domain.payment.dto

import com.ssafy.mmart.domain.gotCart.dto.GotCartRes
import com.ssafy.mmart.domain.payment.Payment
import com.ssafy.mmart.domain.user.User

data class PaymentReq (
    var gotCartRes: GotCartRes,
){
    fun toEntity(user: User): Payment = Payment(
        total = gotCartRes.total,
        user = user,
    )
}