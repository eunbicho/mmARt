package com.ssafy.mmart.exception

import org.springframework.http.HttpStatus

enum class ErrorCode(httpStatus: HttpStatus,message: String) {

    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당하는 사용자를 찾을 수 없습니다."),
    FAVORITE_NOT_FOUND(HttpStatus.NOT_FOUND, "해당하는 즐겨찾기를 찾을 수 없습니다."),
    CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "해당하는 카테고리를 찾을 수 없습니다."),
    PHOTO_NOT_FOUND(HttpStatus.NOT_FOUND, "사진을 찾을 수 없습니다."),
    GETCART_EMPTY(HttpStatus.CONFLICT, "장볼구니가 비어있습니다."),
    GETCART_NOT_FOUND(HttpStatus.NOT_FOUND, "장볼구니에서 해당하는 상품을 찾을 수 없습니다."),
    GOTCART_EMPTY(HttpStatus.CONFLICT, "장봤구니가 비어있습니다."),
    GOTCART_NOT_FOUND(HttpStatus.NOT_FOUND, "장봤구니에서 해당하는 상품을 찾을 수 없습니다."),
    ITEM_NOT_FOUND(HttpStatus.NOT_FOUND, "해당하는 상품을 찾을 수 없습니다."),
    EMAIL_DUPLICATE(HttpStatus.CONFLICT, "중복된 이메일입니다."),
    REVIEW_DUPLICATE(HttpStatus.CONFLICT, "이미 작성된 리뷰가 있습니다."),
    OVER_QUANTITY(HttpStatus.CONFLICT, "재고가 부족합니다."),
    WRONG_QUANTITY(HttpStatus.CONFLICT, "잘못된 수량을 입력하였습니다."),
    REVIEWS_NOT_FOUND(HttpStatus.NOT_FOUND, "리뷰가 없습니다."),
    REVIEW_NOT_FOUND(HttpStatus.NOT_FOUND, "해당하는 리뷰를 찾을 수 없습니다."),
    PAYMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "결제 내역이 없습니다."),
    PAYMENT_DETAIL_NOT_FOUND(HttpStatus.NOT_FOUND, "해당하는 결제 내역을 찾을 수 없습니다."),
    FAVORITE_DUPLICATE(HttpStatus.CONFLICT, "중복된 즐겨찾기입니다."),
    BAD_ACCESS(HttpStatus.BAD_REQUEST, "잘못된 접근입니다."),
    DATABASE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "데이터베이스 에러");

    val httpStatus: HttpStatus = httpStatus
    val message: String = message
}