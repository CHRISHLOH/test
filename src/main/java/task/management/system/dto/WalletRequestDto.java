package task.management.system.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WalletRequestDto implements Serializable {

    @NotNull(message = "Wallet ID cannot be null")
    private UUID walletId;

    @NotNull(message = "Operation type cannot be null")
    private String operationType;

    @Positive(message = "Amount must be greater than 0")
    private Long amount;
}