package com.ssafy.mmart.exception.not_found

import com.ssafy.mmart.exception.AbstractAppException
import com.ssafy.mmart.exception.ErrorCode

class CategoryNotFoundException: AbstractAppException(ErrorCode.CATEGORY_NOT_FOUND)