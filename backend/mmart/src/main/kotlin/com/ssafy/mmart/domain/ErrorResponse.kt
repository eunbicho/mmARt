package com.ssafy.mmart.domain

import com.ssafy.mmart.exception.ErrorCode

class ErrorResponse(errorCode: ErrorCode, msg:String?) {
    val errorCode: String = errorCode.name
    val message: String? = msg ?: errorCode.message
    constructor(errorCode: ErrorCode) : this(errorCode, null)


    companion object {
        fun of(errorCode: ErrorCode): ErrorResponse {
            return ErrorResponse(errorCode)
        }
    }
}