package com.ssafy.mmart.exception
abstract class AbstractAppException(var errorCode: ErrorCode):RuntimeException(errorCode.message) {

//    open fun AbstractAppException(errorCode: ErrorCode) {
//        super(errorCode.getMessage())
//        this.errorCode = errorCode
//    }

}