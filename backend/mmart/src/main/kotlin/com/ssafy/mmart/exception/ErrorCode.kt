package com.ssafy.mmart.exception

import org.springframework.http.HttpStatus

enum class ErrorCode(httpStatus: HttpStatus,message: String) {

    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당하는 유저를 찾을 수 없습니다."),
    EMAIL_DUPLICATE(HttpStatus.CONFLICT, "중복된 이메일입니다."),
    PAYMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "결제 내역이 없습니다."),
    PAYMENT_DETAIL_NOT_FOUND(HttpStatus.NOT_FOUND, "해당하는 결제 내역이 없습니다."),
    BAD_ACCESS(HttpStatus.BAD_REQUEST, "잘못된 접근입니다."),
    DATABASE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "데이터베이스 에러");

    public val httpStatus: HttpStatus = httpStatus
    public val message: String = message
}