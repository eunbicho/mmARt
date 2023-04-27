package com.ssafy.mmart.exception.not_found

import com.ssafy.mmart.exception.AbstractAppException
import com.ssafy.mmart.exception.ErrorCode

class ReviewsNotFoundException: AbstractAppException(ErrorCode.REVIEWS_NOT_FOUND)