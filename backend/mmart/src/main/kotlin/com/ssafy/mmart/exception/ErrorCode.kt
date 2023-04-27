package com.ssafy.mmart.exception

import org.springframework.http.HttpStatus

enum class ErrorCode(httpStatus: HttpStatus,message: String) {

    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당하는 유저를 찾을 수 없습니다."),
    FAVORITE_NOT_FOUND(HttpStatus.NOT_FOUND, "해당하는 즐겨찾기를 찾을 수 없습니다."),
    CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "해당하는 카테고리를 찾을 수 없습니다."),
    ITEM_NOT_FOUND(HttpStatus.NOT_FOUND, "해당하는 상품을 찾을 수 없습니다."),
    EMAIL_DUPLICATE(HttpStatus.CONFLICT, "중복된 이메일입니다."),
    FAVORITE_DUPLICATE(HttpStatus.CONFLICT, "중복된 즐겨찾기입니다."),
    GETCART_EMPTY(HttpStatus.CONFLICT, "장볼구니가 비어있습니다."),
    BAD_ACCESS(HttpStatus.BAD_REQUEST, "잘못된 접근입니다."),
    DATABASE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "데이터베이스 에러");

    public val httpStatus: HttpStatus = httpStatus
    public val message: String = message
}