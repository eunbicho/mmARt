package com.ssafy.mmart.controller

import com.ssafy.mmart.domain.ResultResponse
import com.ssafy.mmart.domain.payment.Payment
import com.ssafy.mmart.domain.payment.dto.PaymentReq
import com.ssafy.mmart.service.PaymentService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api/v1/payments")
class PaymentController @Autowired constructor(
    val paymentService: PaymentService,
) {
   @GetMapping
   fun getPayments(@RequestParam userIdx: Int): ResultResponse<List<Payment>?> {
       return ResultResponse.success(paymentService.getPayments(userIdx))
   }

    @PostMapping
    fun createPayment(@RequestParam userIdx: Int, @RequestBody paymentReq: PaymentReq): ResultResponse<Payment?> {
        return ResultResponse.success(paymentService.createPayment(userIdx, paymentReq))
    }

    @DeleteMapping
    fun deletePayment(@RequestParam paymentIdx: Int, @RequestParam userIdx: Int): ResultResponse<Payment?> {
        return ResultResponse.success(paymentService.deletePayment(paymentIdx, userIdx))
    }
}