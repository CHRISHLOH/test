package task.management.system.service;

import task.management.system.dto.WalletRequestDto;
import task.management.system.model.Wallet;

import java.util.UUID;

public interface WalletService {
    
    Long getWalletBalance(UUID uuid);

    void updateWalletBalance(WalletRequestDto request);

    void saveWalletOperation(WalletRequestDto request, Wallet wallet);
}
