package task.management.system.exception.exceptions.exceptionimpl;

import task.management.system.exception.code.ErrorCode;
import task.management.system.exception.exceptions.CustomException;

public class WalletNotFoundException extends CustomException {

    public WalletNotFoundException(String message) {
        super(ErrorCode.WALLET_NOT_FOUND, message);
    }
}
