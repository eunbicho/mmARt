package com.ssafy.mmart.exception.conflict

import com.ssafy.mmart.exception.AbstractAppException
import com.ssafy.mmart.exception.ErrorCode

class GotCartEmptyException: AbstractAppException(ErrorCode.GOTCART_EMPTY) {

}