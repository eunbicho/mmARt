package com.ssafy.mmart.exception.not_found

import com.ssafy.mmart.exception.AbstractAppException
import com.ssafy.mmart.exception.ErrorCode

class UserNotFoundException: AbstractAppException(ErrorCode.USER_NOT_FOUND)