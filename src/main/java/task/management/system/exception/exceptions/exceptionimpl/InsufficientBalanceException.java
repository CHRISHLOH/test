package task.management.system.exception.exceptions.exceptionimpl;

import task.management.system.exception.code.ErrorCode;
import task.management.system.exception.exceptions.CustomException;

public class InsufficientBalanceException extends CustomException {

    public InsufficientBalanceException(String message) {
        super(ErrorCode.INSUFFICIENT_BALANCE, message);
    }
}