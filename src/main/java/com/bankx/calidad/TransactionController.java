package com.bankx.calidad;
import java.math.BigDecimal;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
/**
 * Expone el endpoint para simular transacciones.
 */
@RestController
@RequestMapping("/api/transactions")
public class TransactionController {
    private final TransactionService transactionService;
    /**
     * Recibe el servicio mediante inyección por constructor.
     *
     * @param transactionService servicio de transacciones
     */
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }
    /**
     * Recibe una solicitud y devuelve el saldo calculado.
     *
     * @param request datos de la transacción
     * @return respuesta exitosa o error de validación
     */
    @PostMapping
    public Mono<ResponseEntity<Map<String, Object>>> create(
            @RequestBody TransactionRequest request) {
        return transactionService.calculate(
                        request.type(), request.amount(), request.balance())
                .map(result -> ResponseEntity.ok(
                        Map.<String, Object>of(
                                "status", "OK",
                                "newBalance", result)))
                .onErrorResume(
                        IllegalArgumentException.class,
                        error -> Mono.just(
                                ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                        .body(Map.<String, Object>of(
                                                "status", "ERROR",
                                                "message", error.getMessage()))));
    }
    /**
     * Datos necesarios para simular una transacción.
     *
     * @param type tipo de operación
     * @param amount monto de la operación
     * @param balance saldo disponible
     */
    public record TransactionRequest(
            String type,
            BigDecimal amount,
            BigDecimal balance) {
    }
}