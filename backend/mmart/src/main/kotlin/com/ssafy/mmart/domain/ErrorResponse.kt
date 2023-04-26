package com.ssafy.mmart.domain

import com.ssafy.mmart.exception.ErrorCode

class ErrorResponse {
    var errorCode: String
    var message: String

    constructor(errorCode: ErrorCode) {
        this.errorCode = errorCode.name
        this.message = errorCode.message
    }

    constructor(errorCode: ErrorCode, msg: String) {
        this.errorCode = errorCode.name
        this.message = msg
    }



    companion object {
        fun of(errorCode: ErrorCode): ErrorResponse {
            return ErrorResponse(errorCode)
        }
    }

    override fun toString(): String {
        return "ErrorResponse(errorCode='$errorCode', message='$message')"
    }
}