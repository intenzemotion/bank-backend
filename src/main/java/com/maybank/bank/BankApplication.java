package com.maybank.bank;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
        info = @Info(
                title = "Maybank Mock App",
                description = "Backend Rest API for Maybank Mock App",
                version = "v1.0",
                contact = @Contact(
                        name = "Wal Ikram Suaimi",
                        email = "wsuaimi@gmail.com",
                        url = "https://github.com/intenzemotion"
                ),
                license = @License(
                        name = "GPL-3.0",
                        url = "https://github.com/intenzemotion"
                )
        ),
        externalDocs = @ExternalDocumentation(
                description = "Backend Rest API Documentation for Maybank",
                url = "https://github.com/intenzemotion"
        )
)
public class BankApplication {

    public static void main(String[] args) {
        SpringApplication.run(BankApplication.class, args);
    }

}
