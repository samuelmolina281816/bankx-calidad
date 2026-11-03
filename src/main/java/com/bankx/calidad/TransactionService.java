package com.bankx.calidad;
import java.math.BigDecimal;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
/**
 * Contiene las reglas básicas de una transacción bancaria.
 */
@Service
public class TransactionService {
    private static final String DEBIT = "DEBIT";
    private static final String CREDIT = "CREDIT";
    private static final BigDecimal MAX_DEBIT = new BigDecimal("1000");
    /**
     * Calcula el saldo resultante de una transacción.
     *
     * @param type tipo de operación: DEBIT o CREDIT
     * @param amount monto de la operación
     * @param balance saldo disponible
     * @return saldo calculado o un error de negocio
     */
    public Mono<BigDecimal> calculate(
            String type, BigDecimal amount, BigDecimal balance) {
        return Mono.defer(() -> {
            if (!DEBIT.equals(type) && !CREDIT.equals(type)) {
                return Mono.error(new IllegalArgumentException("invalid_type"));
            }
            if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
                return Mono.error(new IllegalArgumentException("invalid_amount"));
            }
            if (balance == null || balance.compareTo(BigDecimal.ZERO) < 0) {
                return Mono.error(new IllegalArgumentException("invalid_balance"));
            }
            if (DEBIT.equals(type) && amount.compareTo(MAX_DEBIT) > 0) {
                return Mono.error(new IllegalArgumentException("risk_rejected"));
            }
            if (DEBIT.equals(type) && balance.compareTo(amount) < 0) {
                return Mono.error(new IllegalArgumentException("insufficient_funds"));
            }
            if (DEBIT.equals(type)) {
                return Mono.just(balance.subtract(amount));
            }
            return Mono.just(balance.add(amount));
        });
    }
}