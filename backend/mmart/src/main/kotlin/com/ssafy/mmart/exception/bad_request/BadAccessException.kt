package com.ssafy.mmart.exception.bad_request

import com.ssafy.mmart.exception.AbstractAppException
import com.ssafy.mmart.exception.ErrorCode

class BadAccessException: AbstractAppException(ErrorCode.BAD_ACCESS)