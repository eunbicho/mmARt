package com.ssafy.mmart.exception.not_found

import com.ssafy.mmart.exception.AbstractAppException
import com.ssafy.mmart.exception.ErrorCode

class GetCartNotFoundException: AbstractAppException(ErrorCode.GET_CART_NOT_FOUND)