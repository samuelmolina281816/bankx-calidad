package com.bankx.calidad;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
class TransactionControllerTest {
    @Autowired
    private WebTestClient client;
    @Test
    void debeResponderConSaldoCalculado() {
        TransactionController.TransactionRequest request =
                new TransactionController.TransactionRequest(
                        "DEBIT",
                        new BigDecimal("100"),
                        new BigDecimal("500"));
        client.post()
                .uri("/api/transactions")
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.status").isEqualTo("OK")
                .jsonPath("$.newBalance").isEqualTo(400);
    }
    @Test
    void debeResponderConErrorPorFondosInsuficientes() {
        TransactionController.TransactionRequest request =
                new TransactionController.TransactionRequest(
                        "DEBIT",
                        new BigDecimal("200"),
                        new BigDecimal("100"));
        client.post()
                .uri("/api/transactions")
                .bodyValue(request)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.status").isEqualTo("ERROR")
                .jsonPath("$.message").isEqualTo("insufficient_funds");
    }
}