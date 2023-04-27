package com.ssafy.mmart.exception.not_found

import com.ssafy.mmart.exception.AbstractAppException
import com.ssafy.mmart.exception.ErrorCode

class PaymentDetailNotFoundException: AbstractAppException(ErrorCode.PAYMENT_DETAIL_NOT_FOUND)