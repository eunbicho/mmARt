package com.ssafy.mmart.domain.user.dto

import com.ssafy.mmart.domain.user.User

data class CreateUserReq (
    var userIdx: Int? = null,
    var email: String,
    var password: String,
    var name: String,
    var point: Int? = 0
) {
    fun toEntity(): User = User(
        userIdx = userIdx,
        email = email,
        password = password,
        name = name,
        point = 0
    )
}