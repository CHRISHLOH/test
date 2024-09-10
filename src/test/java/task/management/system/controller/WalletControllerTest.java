package task.management.system.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import task.management.system.dto.WalletRequestDto;
import task.management.system.exception.code.ErrorCode;
import task.management.system.exception.exceptions.exceptionimpl.WalletNotFoundException;
import task.management.system.model.enums.OperationType;
import task.management.system.service.WalletServiceImpl;

import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class WalletControllerTest {

    @Mock
    private WalletServiceImpl walletServiceImpl;

    @InjectMocks
    private WalletController walletController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    private UUID walletId;
    private WalletRequestDto walletRequestDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(walletController).build();
        objectMapper = new ObjectMapper();

        walletId = UUID.randomUUID();
        walletRequestDto = new WalletRequestDto(walletId, OperationType.DEPOSIT.getOperationType(), 100L);
    }

    @Test
    void testUpdateWalletBalance_Success() throws Exception {
        doNothing().when(walletServiceImpl).updateWalletBalance(any(WalletRequestDto.class));

        mockMvc.perform(post("/api/v1/wallets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(walletRequestDto)))
                .andExpect(status().isOk());

        verify(walletServiceImpl, times(1)).updateWalletBalance(any(WalletRequestDto.class));
    }

    @Test
    void testUpdateWalletBalance_InvalidRequest() throws Exception {
        WalletRequestDto invalidRequest = new WalletRequestDto(null, OperationType.DEPOSIT.getOperationType(), 100L);

        // Выполняем POST запрос с некорректными данными
        mockMvc.perform(post("/api/v1/wallets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetWalletBalance_Success() throws Exception {
        when(walletServiceImpl.getWalletBalance(walletId)).thenReturn(100L);

        mockMvc.perform(get("/api/v1/wallets/{walletId}", walletId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(100L));

        verify(walletServiceImpl, times(1)).getWalletBalance(walletId);
    }

    @Test
    void testGetWalletBalance_WalletNotFound() throws Exception {
        when(walletServiceImpl.getWalletBalance(walletId)).thenThrow(new WalletNotFoundException(ErrorCode.WALLET_NOT_FOUND.getDescription()));

        mockMvc.perform(get("/api/v1/wallets/{walletId}", walletId))
                .andExpect(status().isNotFound());
    }
}