package com.ssafy.mmart.exception.not_found

import com.ssafy.mmart.exception.AbstractAppException
import com.ssafy.mmart.exception.ErrorCode

class PhotoNotFoundException: AbstractAppException(ErrorCode.PHOTO_NOT_FOUND)