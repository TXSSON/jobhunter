package vn.sondev.jobhunter1.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import vn.sondev.jobhunter1.domain.response.ApiResponse;
import vn.sondev.jobhunter1.exception.exceptioncustom.AppException;
import vn.sondev.jobhunter1.util.enums.ErrorCodeEnum;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiResponse<Object>> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException exception) {
        ErrorCodeEnum errorCodeEnum = ErrorCodeEnum.USER_NOT_EXISTED;

        return ResponseEntity.status(errorCodeEnum.getStatusCode())
                .body(ApiResponse.builder()
                        .statusCode(errorCodeEnum.getCode())
                        .message(errorCodeEnum.getMessage())
                        .build());
    }
    @ExceptionHandler(value = {
            UsernameNotFoundException.class,
            BadCredentialsException.class
    })
    public ResponseEntity<ApiResponse<Object>> handleIdException(Exception ex) {
        ApiResponse<Object> res = new ApiResponse<Object>();
        res.setStatusCode(HttpStatus.BAD_REQUEST.value());
        res.setMessage("Exception occurs...");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
    }
    @ExceptionHandler(value = AppException.class)
    ResponseEntity<ApiResponse<Object>> handlingAppException(AppException exception) {
        ErrorCodeEnum errorCode = exception.getErrorCode();
        ApiResponse<Object> apiResponse = new ApiResponse<Object>();

        apiResponse.setStatusCode(errorCode.getCode());
        apiResponse.setMessage(errorCode.getMessage());
        return ResponseEntity.status(errorCode.getStatusCode()).body(apiResponse);
    }
}
