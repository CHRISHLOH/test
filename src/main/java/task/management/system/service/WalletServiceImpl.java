package task.management.system.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import task.management.system.dto.WalletRequestDto;
import task.management.system.exception.code.ErrorCode;
import task.management.system.exception.exceptions.exceptionimpl.InsufficientBalanceException;
import task.management.system.exception.exceptions.exceptionimpl.InvalidRequestException;
import task.management.system.exception.exceptions.exceptionimpl.WalletNotFoundException;
import task.management.system.model.enums.OperationType;
import task.management.system.model.Wallet;
import task.management.system.model.WalletOperation;
import task.management.system.repository.WalletOperationRepository;
import task.management.system.repository.WalletRepository;

import java.time.LocalDateTime;
import java.util.UUID;


@Service
@RequiredArgsConstructor
@Slf4j
public class WalletServiceImpl implements WalletService {

    private final WalletRepository walletRepository;
    private final WalletOperationRepository walletOperationRepository;

    @Override
    @Transactional(readOnly = true)
    public Long getWalletBalance(UUID uuid) {
        log.info("Запрос на получение баланса для кошелька с ID: {}", uuid);
        return walletRepository.findById(uuid).orElseThrow(() ->
                        new WalletNotFoundException(ErrorCode.WALLET_NOT_FOUND.getDescription()))
                .getBalance();
    }

    @Override
    @Retryable(
            maxAttempts = 10,
            backoff = @Backoff(delay = 200))
    @Transactional
    public void updateWalletBalance(WalletRequestDto request) {
        log.info("Запрос на обновление баланса: {}", request);

        Wallet wallet = walletRepository
                .findById(request.getWalletId())
                .orElseThrow(() ->
                        new WalletNotFoundException(ErrorCode.WALLET_NOT_FOUND.getDescription()));

        if (request.getOperationType().equals(OperationType.DEPOSIT.getOperationType())) {
            log.info("Внесение средств в кошелек: {}", wallet.getId());

            wallet.setBalance(wallet.getBalance() + request.getAmount());
            walletRepository.save(wallet);
            saveWalletOperation(request, wallet);
        } else if (request.getOperationType().equals(OperationType.WITHDRAW.getOperationType())) {
            log.info("Снятие средств с кошелька: {}", wallet.getId());

            if (wallet.getBalance() < request.getAmount()) {
                throw new InsufficientBalanceException(ErrorCode.INSUFFICIENT_BALANCE.getDescription());
            }
            wallet.setBalance(wallet.getBalance() - request.getAmount());
            walletRepository.save(wallet);
            saveWalletOperation(request, wallet);
        } else {
            log.error("Неверный тип операции: {}", request.getOperationType());

            throw new InvalidRequestException(ErrorCode.INVALID_REQUEST.getDescription());
        }
    }

    @Override
    public void saveWalletOperation(WalletRequestDto request, Wallet wallet) {
        log.info("Сохранение операции с кошельком: {}. Тип операции: {}, Сумма: {}",
                wallet.getId(), request.getOperationType(), request.getAmount());

        WalletOperation operation = WalletOperation.builder()
                .operationType(request.getOperationType())
                .amount(request.getAmount())
                .createdAt(LocalDateTime.now())
                .wallet(wallet)
                .build();
        walletOperationRepository.save(operation);
    }
}