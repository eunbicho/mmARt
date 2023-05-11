package com.ssafy.mmart.domain.user.dto

import com.ssafy.mmart.domain.user.User

data class UserRes(
    var userIdx: Int,
    var email: String,
    var password: String,
    var name: String,
) {
//    fun toEntity(user: User): UserRes = UserRes(
//        userIdx = user.userIdx!!,
//        email = user.email,
//        password = user.password,
//        name = user.name
//    )
}