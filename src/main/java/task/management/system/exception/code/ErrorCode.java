package task.management.system.exception.code;

import lombok.Getter;

@Getter
public enum ErrorCode {
    WALLET_NOT_FOUND("WALLET_NOT_FOUND", "Wallet not found"),
    INSUFFICIENT_BALANCE("INSUFFICIENT_BALANCE", "Not enough balance"),
    INVALID_REQUEST("INVALID_REQUEST", "Invalid request"),
    INTERNAL_ERROR("INTERNAL_ERROR", "Internal server error");

    private final String code;
    private final String description;

    ErrorCode(String code, String description) {
        this.code = code;
        this.description = description;
    }
}
