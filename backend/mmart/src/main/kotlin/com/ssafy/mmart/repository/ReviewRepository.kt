package com.ssafy.mmart.repository

import com.ssafy.mmart.domain.paymentDetail.PaymentDetail
import com.ssafy.mmart.domain.review.Review
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ReviewRepository : JpaRepository<Review, Int> {
    fun findAllByUser_UserIdx(userIdx: Int): List<Review>?
    fun findAllByItem_ItemIdx(itemIdx: Int): List<Review>?
    fun findByPaymentDetail(paymentDetail: PaymentDetail): Review?
    fun existsByPaymentDetail_PaymentDetailIdxAndItem_ItemIdx(paymentDetailIdx: Int,itemIdx: Int):Boolean
}