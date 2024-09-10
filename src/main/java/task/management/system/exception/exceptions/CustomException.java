package task.management.system.exception.exceptions;

import lombok.Getter;
import task.management.system.exception.code.ErrorCode;

@Getter
public abstract class CustomException extends RuntimeException {

    private final String code;

    public CustomException(ErrorCode errorCode, String message) {
        super(message);
        this.code = errorCode.getCode();
    }

}