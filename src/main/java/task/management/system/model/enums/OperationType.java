package task.management.system.model.enums;

import lombok.Getter;

@Getter
public enum OperationType {

    WITHDRAW("WITHDRAW"),
    DEPOSIT("DEPOSIT");

    private final String operationType;

    OperationType(String operationType) {
        this.operationType = operationType;
    }
}
