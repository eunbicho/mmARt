package com.ssafy.mmart.service

import com.ssafy.mmart.domain.payment.Payment
import com.ssafy.mmart.exception.not_found.PaymentNotFoundException
import com.ssafy.mmart.exception.not_found.UserNotFoundException
import com.ssafy.mmart.repository.PaymentRepository
import com.ssafy.mmart.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class PaymentService @Autowired constructor(
    val paymentRepository: PaymentRepository,
    val userRepository: UserRepository,
){
    fun getPayments(userIdx: Int): List<Payment>? {
        userRepository.findByIdOrNull(userIdx) ?: throw UserNotFoundException()
        var payments = paymentRepository.findAllByUser_UserIdx(userIdx)
        return if (payments.isNullOrEmpty()) {
            throw PaymentNotFoundException()
        } else {
            payments
        }
    }
}