package com.ssafy.mmart.domain

import com.ssafy.mmart.exception.AbstractAppException
import com.ssafy.mmart.exception.ErrorCode

class ResultResponse<T>(var resultCode:String?,var result:T?) {

companion object {
    val SUCCESS = "SUCCESS"
    val ERROR = "ERROR"
    fun <T> success(result: T): ResultResponse<T> {
        return ResultResponse(
            SUCCESS,
            result
        )
    }

    fun error(errorResponse: ErrorResponse): ResultResponse<ErrorResponse> {
        return ResultResponse(
            ERROR,
            errorResponse
        )
    }


    fun error(e: AbstractAppException): ResultResponse<ErrorResponse> {
        return ResultResponse(ERROR, ErrorResponse.of(e.errorCode))
    }
}

    override fun toString(): String {
        return "ResultResponse(resultCode=$resultCode, result=$result)"
    }
}

/*package com.ssafy.snapstory.domain;

import lombok.Getter;

@Getter
public class ResultResponse<T> {
    private static final String SUCCESS = "SUCCESS";
    private static final String ERROR = "ERROR";
    private final String resultCode;
    private final T result;

    public ResultResponse(String resultCode, T result) {
        this.resultCode = resultCode;
        this.result = result;
    }

    public static <T> ResultResponse<T> success(T result) {
        return new ResultResponse<>(SUCCESS, result);
    }

    public static ResultResponse<ErrorResponse> error(ErrorResponse errorResponse) {
        return new ResultResponse<>(ERROR, errorResponse);
    }

    public static ResultResponse<ErrorResponse> error(AbstractAppException e) {
        return new ResultResponse<>(ERROR, ErrorResponse.of(e.getErrorCode()));
    }
}
*/