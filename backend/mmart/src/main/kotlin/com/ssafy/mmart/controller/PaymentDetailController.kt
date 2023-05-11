package com.ssafy.mmart.controller

import com.ssafy.mmart.domain.ResultResponse
import com.ssafy.mmart.domain.paymentDetail.PaymentDetail
import com.ssafy.mmart.domain.paymentDetail.dto.PaymentDetailRes
import com.ssafy.mmart.service.PaymentDetailService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("api/v1/payments/detail")
class PaymentDetailController @Autowired constructor(
    val paymentDetailService: PaymentDetailService,
){
    @GetMapping
    fun getPaymentDetail(@RequestParam paymentIdx: Int, @RequestParam userIdx: Int): ResultResponse<List<PaymentDetailRes>?>{
        return ResultResponse.success(paymentDetailService.getPaymentDetails(paymentIdx, userIdx))
    }

    @GetMapping("/{paymentDetailIdx}")
    fun getPaymentDetail(@PathVariable paymentDetailIdx: Int): ResultResponse<PaymentDetail?>{
        return ResultResponse.success(paymentDetailService.getPaymentDetail(paymentDetailIdx))
    }
}