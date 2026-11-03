package com.bankx.calidad;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
/**
 * Punto de inicio del microservicio.
 */
@SpringBootApplication
public class BankxApplication {
    /**
     * Inicia la aplicación Spring Boot.
     *
     * @param args argumentos recibidos desde la línea de comandos
     */
    public static void main(String[] args) {
        SpringApplication.run(BankxApplication.class, args);
    }
}