package com.ssafy.mmart.exception.not_found

import com.ssafy.mmart.exception.AbstractAppException
import com.ssafy.mmart.exception.ErrorCode

class GotCartNotFoundException: AbstractAppException(ErrorCode.GOTCART_NOT_FOUND)