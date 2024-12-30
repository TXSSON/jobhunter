package vn.sondev.jobhunter1.exception.exceptioncustom;

import lombok.Getter;
import lombok.Setter;
import vn.sondev.jobhunter1.util.enums.ErrorCodeEnum;

@Getter
@Setter
public class AppException extends RuntimeException {
    private ErrorCodeEnum errorCode;
    public AppException(ErrorCodeEnum errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
    public AppException(ErrorCodeEnum errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
}
