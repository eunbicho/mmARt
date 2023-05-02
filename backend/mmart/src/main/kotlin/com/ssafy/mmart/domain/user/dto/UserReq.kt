package com.ssafy.mmart.domain.user.dto

import com.google.zxing.qrcode.encoder.QRCode
import com.ssafy.mmart.domain.user.User

data class UserReq (
    var email: String,
    var password: String,
    var name: String,
) {
    fun toEntity(qrCode: String): User = User(
        email = email,
        password = password,
        name = name,
        qrcode = qrCode,
    )
}