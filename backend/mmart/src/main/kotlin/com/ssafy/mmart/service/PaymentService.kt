package com.ssafy.mmart.service

import com.ssafy.mmart.domain.payment.Payment
import com.ssafy.mmart.domain.payment.dto.PaymentReq
import com.ssafy.mmart.domain.paymentDetail.PaymentDetail
import com.ssafy.mmart.exception.bad_request.BadAccessException
import com.ssafy.mmart.exception.not_found.ItemNotFoundException
import com.ssafy.mmart.exception.not_found.PaymentNotFoundException
import com.ssafy.mmart.exception.not_found.UserNotFoundException
import com.ssafy.mmart.repository.ItemRepository
import com.ssafy.mmart.repository.PaymentDetailRepository
import com.ssafy.mmart.repository.PaymentRepository
import com.ssafy.mmart.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PaymentService @Autowired constructor(
    val paymentRepository: PaymentRepository,
    val userRepository: UserRepository,
    val itemRepository: ItemRepository,
    val paymentDetailRepository: PaymentDetailRepository,
    val gotCartService: GotCartService,
    val getCartService: GetCartService,
){
    fun getPayments(userIdx: Int): List<Payment>? {//date 역순으로 주기
        userRepository.findByIdOrNull(userIdx) ?: throw UserNotFoundException()
        return paymentRepository.findAllByUser_UserIdxOrderByDateDesc(userIdx)
    }

    fun getPayment(userIdx: Int, paymentIdx: Int): Payment? {
        userRepository.findByIdOrNull(userIdx) ?: throw UserNotFoundException()
        return paymentRepository.findByIdOrNull(paymentIdx) ?: throw PaymentNotFoundException()
    }

    fun createPayment(userIdx: Int): Payment? {
        val user = userRepository.findByIdOrNull(userIdx) ?: throw UserNotFoundException()
        val gotCartRes = gotCartService.getGotCarts(userIdx)
        val payment = paymentRepository.save(Payment(user=user, total = gotCartRes.total))
        for (gotCartItem in gotCartRes.itemList) {
            val item = itemRepository.findByIdOrNull(gotCartItem.itemIdx) ?: throw ItemNotFoundException()
            item.inventory -= gotCartItem.quantity
            val paymentDetail = PaymentDetail(
                quantity = gotCartItem.quantity,
                discount = if (gotCartItem.isCoupon) gotCartItem.price - gotCartItem.couponPrice else 0,
                totalPrice = gotCartItem.couponPrice * gotCartItem.quantity,
                payment = payment,
                item = item,
            )
            paymentDetailRepository.save(paymentDetail)
        }
        gotCartService.deleteGotCarts(userIdx)
        getCartService.deleteGetCarts(userIdx)
        return payment
    }

    @Transactional
    fun deletePayment(paymentIdx: Int, userIdx: Int): Payment? {
        val user = userRepository.findByIdOrNull(userIdx) ?: throw UserNotFoundException()
        val payment = paymentRepository.findByIdOrNull(paymentIdx) ?: throw PaymentNotFoundException()
        if (payment.user == user) {
            paymentRepository.deleteById(payment.paymentIdx!!)
            return payment
        } else {
            throw BadAccessException()
        }
    }
}