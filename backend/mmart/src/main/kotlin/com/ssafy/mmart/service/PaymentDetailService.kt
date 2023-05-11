package com.ssafy.mmart.service

import com.ssafy.mmart.domain.paymentDetail.PaymentDetail
import com.ssafy.mmart.domain.paymentDetail.dto.PaymentDetailRes
import com.ssafy.mmart.exception.not_found.PaymentDetailNotFoundException
import com.ssafy.mmart.exception.not_found.UserNotFoundException
import com.ssafy.mmart.repository.PaymentDetailRepository
import com.ssafy.mmart.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class PaymentDetailService @Autowired constructor(
    val paymentDetailRepository: PaymentDetailRepository,
    val userRepository: UserRepository,
    val itemService: ItemService,
) {
    fun getPaymentDetails(paymentIdx: Int, userIdx: Int): List<PaymentDetailRes>? {
        val user = userRepository.findByIdOrNull(userIdx) ?: throw UserNotFoundException()
        val result = paymentDetailRepository.findAllByPayment_PaymentIdxAndPayment_User(paymentIdx, user) ?: throw PaymentDetailNotFoundException()
        var list = mutableListOf<PaymentDetailRes>()
        result.forEach { paymentDetail ->
            list.add(PaymentDetailRes(paymentDetail.paymentDetailIdx!!,paymentDetail.quantity,paymentDetail.discount,paymentDetail.totalPrice,itemService.setItemRes(paymentDetail.item)!!))

        }
        return list
    }

    fun getPaymentDetail(paymentDetailIdx: Int): PaymentDetail? {
        return paymentDetailRepository.findByIdOrNull(paymentDetailIdx)?: throw PaymentDetailNotFoundException()
    }
}