package com.ssafy.mmart.domain

import com.ssafy.mmart.exception.AbstractAppException

class ResultResponse<T>(var resultCode:String?,var result:T?) {
//    fun <T> success(result: T): ResultResponse<T>? {
//        return ResultResponse<T>(SUCCESS, result)
//    }
//
//    fun <T> error(errorResponse: ErrorResponse): ResultResponse<T>? {
//        return ResultResponse<T>(ERROR, errorResponse)
//    }
//
//    fun <T> error(e: AbstractAppException): ResultResponse<T>? {
//        return ResultResponse<T>(ERROR, )
//    }
companion object {
    fun <T> success(result: T): ResultResponse<T> {
        return ResultResponse("SUCCESS", result)
    }

    fun error(errorResponse: ErrorResponse?): ResultResponse<ErrorResponse> {
        return ResultResponse("ERROR", errorResponse)
    }

    fun error(e: AbstractAppException): ResultResponse<ErrorResponse> {
        return ResultResponse("ERROR", ErrorResponse.of(e.errorCode))
    }
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