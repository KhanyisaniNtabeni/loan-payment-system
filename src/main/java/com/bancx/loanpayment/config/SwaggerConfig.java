package com.bancx.loanpayment.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for Swagger/OpenAPI documentation.
 *
 * Sets up basic API info such as title, version, and description.
 * This configuration enables the Swagger UI to document all
 * REST endpoints exposed by the Loan Payment System.
 *
 * Author: Khanyisani Luyanda Ntabeni
 */
@Configuration
public class SwaggerConfig {

    /**
     * Creates a custom OpenAPI bean with API metadata.
     *
     * @return OpenAPI instance configured with API info
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Loan Payment API")
                        .version("1.0")
                        .description("API documentation for the Loan Payment System")
                        .contact(new Contact()
                                .name("Khanyisani Luyanda Ntabeni")
                                .email("your.klntabeni@gmail.com")
                        ));
    }
}
