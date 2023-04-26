package com.ssafy.mmart.service

import com.ssafy.mmart.domain.payment.Payment
import com.ssafy.mmart.exception.not_found.PaymentNotFoundException
import com.ssafy.mmart.repository.PaymentRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class PaymentService @Autowired constructor(
    val paymentRepository: PaymentRepository,
){
    fun getPayments(userIdx: Int): List<Payment>? {
        var payments = paymentRepository.findAllByUser_UserIdx(userIdx)
        return if (payments.isNullOrEmpty()) {
            throw PaymentNotFoundException()
        } else {
            payments
        }
    }
}