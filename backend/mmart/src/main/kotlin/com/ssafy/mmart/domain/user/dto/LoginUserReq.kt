package com.ssafy.mmart.domain.user.dto

import com.google.zxing.qrcode.encoder.QRCode
import com.ssafy.mmart.domain.user.User

data class LoginUserReq (
    var email: String,
    var password: String,
)