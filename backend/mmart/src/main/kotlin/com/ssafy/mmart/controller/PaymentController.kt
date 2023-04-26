package com.ssafy.mmart.controller

import com.ssafy.mmart.domain.ResultResponse
import com.ssafy.mmart.domain.payment.Payment
import com.ssafy.mmart.service.PaymentService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api/v1/payments")
class PaymentController @Autowired constructor(
    val paymentService: PaymentService,
) {
   @GetMapping
   fun getPayments(userIdx: Int): ResultResponse<List<Payment>?> {
       return ResultResponse.success(paymentService.getPayments(userIdx))
   }
}