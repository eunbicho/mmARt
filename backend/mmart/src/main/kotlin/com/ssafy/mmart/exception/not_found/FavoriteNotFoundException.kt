package com.ssafy.mmart.exception.not_found

import com.ssafy.mmart.exception.AbstractAppException
import com.ssafy.mmart.exception.ErrorCode

class FavoriteNotFoundException: AbstractAppException(ErrorCode.FAVORITE_NOT_FOUND)