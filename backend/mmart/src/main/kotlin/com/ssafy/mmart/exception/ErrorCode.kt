package com.ssafy.mmart.exception

import org.springframework.http.HttpStatus

enum class ErrorCode(var httpStatus: HttpStatus?, var message: String?) {

    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당하는 유저를 찾을 수 없습니다."),
    BAD_ACCESS(HttpStatus.BAD_REQUEST, "잘못된 접근입니다."),
    DATABASE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "데이터베이스 에러");
}