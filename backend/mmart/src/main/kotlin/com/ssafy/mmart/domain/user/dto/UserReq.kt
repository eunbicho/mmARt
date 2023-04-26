package com.ssafy.mmart.domain.user.dto

import com.ssafy.mmart.domain.user.User

data class UserReq (
    var email: String,
    var password: String,
    var name: String,
) {
    fun toEntity(): User = User(
        email = email,
        password = password,
        name = name,
    )
}