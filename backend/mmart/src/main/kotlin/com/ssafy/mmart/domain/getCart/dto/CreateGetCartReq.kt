package com.ssafy.mmart.domain.getCart.dto

import javax.print.attribute.standard.PrintQuality

data class CreateGetCartReq(
    val userIdx:Int,
    val itemIdx:Int,
    val quality:Int,
)