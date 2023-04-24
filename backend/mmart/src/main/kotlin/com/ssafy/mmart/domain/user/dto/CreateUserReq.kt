package com.ssafy.mmart.domain.user.dto

import com.ssafy.mmart.domain.user.User

data class CreateUserReq (
    var user_idx: Int? = null,
    var email: String,
    var password: String,
    var name: String,
    var point: Int? = 0
) {
    fun toEntity(): User = User(
        user_idx = user_idx,
        email = email,
        password = password,
        name = name,
        point = 0
    )
}