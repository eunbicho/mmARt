package com.ssafy.mmart.repository

import com.ssafy.mmart.domain.payment.Payment
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PaymentRepository : JpaRepository<Payment, Int> {
    fun findAllByUser_UserIdxOrderByDateDesc(userIdx: Int): List<Payment>?
}