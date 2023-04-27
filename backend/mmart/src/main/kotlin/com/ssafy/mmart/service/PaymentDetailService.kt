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
    fun getPaymentDetail(paymentIdx: Int, userIdx: Int): PaymentDetail? {
        userRepository.findByIdOrNull(userIdx) ?: throw UserNotFoundException()
        return paymentDetailRepository.findByPayment_PaymentIdxAndPayment_User_UserIdx(paymentIdx, userIdx) ?: throw PaymentDetailNotFoundException()
    }

    @Transactional
    fun deletePaymentDetail(paymentIdx: Int, userIdx: Int): PaymentDetail? {
        userRepository.findByIdOrNull(userIdx) ?: throw UserNotFoundException()
        var paymentDetail = paymentDetailRepository.findByPayment_PaymentIdxAndPayment_User_UserIdx(paymentIdx, userIdx) ?: throw PaymentDetailNotFoundException()
        paymentDetailRepository.deleteById(paymentDetail.paymentDetailIdx!!)
        return paymentDetail
    }
}