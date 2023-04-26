package com.ssafy.mmart.exception.not_found

import com.ssafy.mmart.exception.AbstractAppException
import com.ssafy.mmart.exception.ErrorCode

class PaymentNotFoundException: AbstractAppException(ErrorCode.PAYMENT_NOT_FOUND)