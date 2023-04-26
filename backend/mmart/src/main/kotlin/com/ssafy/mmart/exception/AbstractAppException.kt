package com.ssafy.mmart.exception
open class AbstractAppException(errorCode: ErrorCode):RuntimeException(errorCode.message) {
    public val errorCode:ErrorCode = errorCode
//    open fun AbstractAppException(errorCode: ErrorCode) {
//        super(errorCode.getMessage())
//        this.errorCode = errorCode
//    }

}