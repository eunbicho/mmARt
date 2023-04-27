package com.ssafy.mmart.service

import com.ssafy.mmart.domain.paymentDetail.PaymentDetail
import com.ssafy.mmart.exception.not_found.PaymentDetailNotFoundException
import com.ssafy.mmart.exception.not_found.UserNotFoundException
import com.ssafy.mmart.repository.PaymentDetailRepository
import com.ssafy.mmart.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PaymentDetailService @Autowired constructor(
    val paymentDetailRepository: PaymentDetailRepository,
    val userRepository: UserRepository,
) {
    fun getPaymentDetails(paymentIdx: Int, userIdx: Int): List<PaymentDetail>? {
        var user = userRepository.findByIdOrNull(userIdx) ?: throw UserNotFoundException()
        return paymentDetailRepository.findAllByPayment_PaymentIdxAndPayment_User(paymentIdx, user) ?: throw PaymentDetailNotFoundException()
    }

    fun getPaymentDetail(paymentDetailIdx: Int): PaymentDetail? {
        return paymentDetailRepository.findByIdOrNull(paymentDetailIdx)?: throw PaymentDetailNotFoundException()
    }
}