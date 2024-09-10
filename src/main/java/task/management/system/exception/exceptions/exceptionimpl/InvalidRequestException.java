package task.management.system.exception.exceptions.exceptionimpl;

import task.management.system.exception.code.ErrorCode;
import task.management.system.exception.exceptions.CustomException;

public class InvalidRequestException extends CustomException {

    public InvalidRequestException(String message) {
        super(ErrorCode.INVALID_REQUEST, message);
    }
}
