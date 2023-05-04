package com.ssafy.mmart.exception

import com.ssafy.mmart.domain.ErrorResponse
import com.ssafy.mmart.domain.ResultResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import javax.persistence.PersistenceException
@RestControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(AbstractAppException::class)
    fun abstractBaseExceptionHandler(e: AbstractAppException): ResponseEntity<ResultResponse<ErrorResponse>> {
//        log.error("{} {}", e.getErrorCode().name(), e.getMessage())
        return ResponseEntity.status(e.errorCode.httpStatus)
            .body(ResultResponse.error(e))
    }

    @ExceptionHandler(PersistenceException::class)
    fun persistenceException(e: PersistenceException): ResponseEntity<ResultResponse<ErrorResponse>> {
//        log.error("{} {}", ErrorCode.DATABASE_ERROR.name, ErrorCode.DATABASE_ERROR.getMessage())
        e.printStackTrace()
        return ResponseEntity.status(ErrorCode.DATABASE_ERROR.httpStatus)
            .body(ResultResponse.error(ErrorResponse.of(ErrorCode.DATABASE_ERROR)))
    }
}