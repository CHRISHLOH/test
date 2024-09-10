package task.management.system.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
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

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class WalletServiceImplTest {

    @Mock
    private WalletRepository walletRepository;

    @Mock
    private WalletOperationRepository walletOperationRepository;

    @InjectMocks
    private WalletServiceImpl walletServiceImpl;

    private UUID walletId;
    private Wallet wallet;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        walletId = UUID.randomUUID();
        wallet = new Wallet();
        wallet.setId(walletId);
        wallet.setBalance(100L);
    }

    @Test
    void testGetWalletBalance_Success() {
        when(walletRepository.findById(walletId)).thenReturn(Optional.of(wallet));
        Long balance = walletServiceImpl.getWalletBalance(walletId);

        assertEquals(100L, balance);
        verify(walletRepository, times(1)).findById(walletId);
    }

    @Test
    void testGetWalletBalance_WalletNotFound() {
        when(walletRepository.findById(walletId)).thenReturn(Optional.empty());

        assertThrows(WalletNotFoundException.class, () -> walletServiceImpl.getWalletBalance(walletId));
        verify(walletRepository, times(1)).findById(walletId);
    }

    @Test
    void testUpdateWalletBalance_Deposit_Success() {
        when(walletRepository.findById(walletId)).thenReturn(Optional.of(wallet));

        WalletRequestDto request = new WalletRequestDto(walletId, OperationType.DEPOSIT.getOperationType(), 50L);
        walletServiceImpl.updateWalletBalance(request);

        assertEquals(150L, wallet.getBalance());
        verify(walletRepository, times(1)).save(wallet);
        verify(walletOperationRepository, times(1)).save(any(WalletOperation.class));
    }

    @Test
    void testUpdateWalletBalance_Withdraw_Success() {
        when(walletRepository.findById(walletId)).thenReturn(Optional.of(wallet));

        WalletRequestDto request = new WalletRequestDto(walletId, OperationType.WITHDRAW.getOperationType(), 50L);
        walletServiceImpl.updateWalletBalance(request);

        assertEquals(50L, wallet.getBalance());
        verify(walletRepository, times(1)).save(wallet);
        verify(walletOperationRepository, times(1)).save(any(WalletOperation.class));
    }

    @Test
    void testUpdateWalletBalance_Withdraw_InsufficientBalance() {
        when(walletRepository.findById(walletId)).thenReturn(Optional.of(wallet));

        WalletRequestDto request = new WalletRequestDto(walletId, OperationType.WITHDRAW.getOperationType(), 150L);

        assertThrows(InsufficientBalanceException.class, () -> walletServiceImpl.updateWalletBalance(request));
        verify(walletRepository, never()).save(wallet);
        verify(walletOperationRepository, never()).save(any(WalletOperation.class));
    }

    @Test
    void testUpdateWalletBalance_InvalidRequest() {
        when(walletRepository.findById(walletId)).thenReturn(Optional.of(wallet));

        WalletRequestDto request = new WalletRequestDto(walletId, ErrorCode.INVALID_REQUEST.getCode(), 50L);

        assertThrows(InvalidRequestException.class, () -> walletServiceImpl.updateWalletBalance(request));
        verify(walletRepository, never()).save(wallet);
        verify(walletOperationRepository, never()).save(any(WalletOperation.class));
    }
}
