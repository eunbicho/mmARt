package com.ssafy.mmart.exception.not_found

import com.ssafy.mmart.exception.AbstractAppException
import com.ssafy.mmart.exception.ErrorCode

class ReviewNotFoundException: AbstractAppException(ErrorCode.REVIEW_NOT_FOUND)