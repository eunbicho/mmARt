package com.ssafy.mmart.service

import com.ssafy.mmart.domain.paymentDetail.PaymentDetail
import com.ssafy.mmart.repository.PaymentDetailRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PaymentDetailService @Autowired constructor(
    val paymentDetailRepository: PaymentDetailRepository,
) {
    fun getPaymentDetail(paymentIdx: Int, userIdx: Int): PaymentDetail? {
        return paymentDetailRepository.findByPayment_PaymentIdxAndPayment_User_UserIdx(paymentIdx, userIdx) ?: throw Exception()
    }

    @Transactional
    fun deletePaymentDetail(paymentIdx: Int, userIdx: Int): PaymentDetail? {
        var paymentDetail = paymentDetailRepository.findByPayment_PaymentIdxAndPayment_User_UserIdx(paymentIdx, userIdx) ?: throw Exception()
        paymentDetailRepository.deleteById(paymentDetail.paymentDetailIdx!!)
        return paymentDetail
    }
}