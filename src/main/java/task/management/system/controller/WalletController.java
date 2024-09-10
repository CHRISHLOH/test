package task.management.system.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import task.management.system.dto.WalletRequestDto;
import task.management.system.service.WalletServiceImpl;

import java.util.UUID;
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/wallets")
@Tag(name = "Wallet API", description = "API для управления кошельками")
public class WalletController {

    private final WalletServiceImpl walletServiceImpl;

    @PostMapping
    @Operation(
            summary = "Update wallet balance",
            description = "Обновляет баланс кошелька на основе запроса.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(
                                    implementation = WalletRequestDto.class
                            )
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Запрос на обновление баланса успешно отправлен",
                            content = @Content(
                                    mediaType = MediaType.TEXT_PLAIN_VALUE,
                                    schema = @Schema(type = "string")
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Неверные данные запроса",
                            content = @Content(
                                    mediaType = MediaType.TEXT_PLAIN_VALUE,
                                    schema = @Schema(type = "string")
                            )
                    )
            }
    )
    public ResponseEntity<String> updateWalletBalance(@Valid @RequestBody WalletRequestDto request) {
            walletServiceImpl.updateWalletBalance(request);
            log.info("Баланс кошелька с ID: {} успешно обновлён", request.getWalletId());
            return ResponseEntity.ok().build();
    }

    @GetMapping("/{walletId}")
    @Operation(
            summary = "Get wallet balance",
            description = "Получает текущий баланс кошелька по его ID.",
            parameters = {
                    @io.swagger.v3.oas.annotations.Parameter(
                            name = "walletId",
                            description = "Идентификатор кошелька",
                            required = true,
                            schema = @Schema(type = "string", format = "uuid")
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Баланс кошелька успешно получен",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(type = "number", format = "long")
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Кошелек не найден",
                            content = @Content(
                                    mediaType = MediaType.TEXT_PLAIN_VALUE,
                                    schema = @Schema(type = "string")
                            )
                    )
            }
    )
    public ResponseEntity<Long> getWalletBalance(@PathVariable UUID walletId) {
            Long balance = walletServiceImpl.getWalletBalance(walletId);
            log.info("Баланс кошелька с ID: {} успешно получен: {}", walletId, balance);
            return ResponseEntity.ok(balance);
    }
}
