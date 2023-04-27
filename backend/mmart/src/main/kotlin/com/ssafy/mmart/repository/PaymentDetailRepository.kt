package com.ssafy.mmart.repository

import com.ssafy.mmart.domain.paymentDetail.PaymentDetail
import com.ssafy.mmart.domain.user.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PaymentDetailRepository : JpaRepository<PaymentDetail, Int> {
    fun findAllByPayment_PaymentIdxAndPayment_User(paymentIdx: Int, user: User): List<PaymentDetail>?
}