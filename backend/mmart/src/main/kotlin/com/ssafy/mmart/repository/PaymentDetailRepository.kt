package com.ssafy.mmart.repository

import com.ssafy.mmart.domain.paymentDetail.PaymentDetail
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PaymentDetailRepository : JpaRepository<PaymentDetail, Int> {

    fun findByPayment_PaymentIdxAndPayment_User_UserIdx(paymentIdx: Int, userIdx: Int): PaymentDetail?
}