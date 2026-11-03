package com.bankx.calidad;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;
class TransactionServiceTest {
    private final TransactionService service = new TransactionService();
    @Test
    void debeProcesarDebitoCorrectamente() {
        StepVerifier.create(
                        service.calculate(
                                "DEBIT",
                                new BigDecimal("100"),
                                new BigDecimal("500")))
                .expectNext(new BigDecimal("400"))
                .verifyComplete();
    }
    @Test
    void debeProcesarCreditoCorrectamente() {
        StepVerifier.create(
                        service.calculate(
                                "CREDIT",
                                new BigDecimal("1500"),
                                new BigDecimal("500")))
                .expectNext(new BigDecimal("2000"))
                .verifyComplete();
    }
    @Test
    void debeRechazarDebitoPorRiesgo() {
        StepVerifier.create(
                        service.calculate(
                                "DEBIT",
                                new BigDecimal("1001"),
                                new BigDecimal("5000")))
                .expectErrorMessage("risk_rejected")
                .verify();
    }
    @Test
    void debeRechazarDebitoPorFondosInsuficientes() {
        StepVerifier.create(
                        service.calculate(
                                "DEBIT",
                                new BigDecimal("200"),
                                new BigDecimal("100")))
                .expectErrorMessage("insufficient_funds")
                .verify();
    }
    @Test
    void debeRechazarTipoDesconocido() {
        StepVerifier.create(
                        service.calculate(
                                "TRANSFER",
                                new BigDecimal("100"),
                                new BigDecimal("500")))
                .expectErrorMessage("invalid_type")
                .verify();
    }
    @Test
    void debeRechazarTipoNulo() {
        StepVerifier.create(
                        service.calculate(
                                null,
                                new BigDecimal("100"),
                                new BigDecimal("500")))
                .expectErrorMessage("invalid_type")
                .verify();
    }
    @Test
    void debeRechazarMontoNulo() {
        StepVerifier.create(
                        service.calculate(
                                "DEBIT",
                                null,
                                new BigDecimal("500")))
                .expectErrorMessage("invalid_amount")
                .verify();
    }
    @Test
    void debeRechazarMontoEnCero() {
        StepVerifier.create(
                        service.calculate(
                                "DEBIT",
                                BigDecimal.ZERO,
                                new BigDecimal("500")))
                .expectErrorMessage("invalid_amount")
                .verify();
    }
    @Test
    void debeRechazarSaldoNulo() {
        StepVerifier.create(
                        service.calculate(
                                "DEBIT",
                                new BigDecimal("100"),
                                null))
                .expectErrorMessage("invalid_balance")
                .verify();
    }
    @Test
    void debeRechazarSaldoNegativo() {
        StepVerifier.create(
                        service.calculate(
                                "DEBIT",
                                new BigDecimal("100"),
                                new BigDecimal("-1")))
                .expectErrorMessage("invalid_balance")
                .verify();
    }
    @Test
    void debePermitirDebitoEnElLimiteDeRiesgo() {
        StepVerifier.create(
                        service.calculate(
                                "DEBIT",
                                new BigDecimal("1000"),
                                new BigDecimal("1000")))
                .expectNext(BigDecimal.ZERO)
                .verifyComplete();
    }
}