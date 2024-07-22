package com.example.casestudy.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;

public class SwaggerConfig {
    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("default") // API grubu adı
                .pathsToMatch("/**") // Bu path'lere sahip API'leri dahil et
                .build();
    }

    @Bean
    public GroupedOpenApi userApi() {
        return GroupedOpenApi.builder()
                .group("user-api") // API grubu adı
                .pathsToMatch("/api/auth/users/**") // Kullanıcı API'lerini dahil et
                .build();
    }

    @Bean
    public GroupedOpenApi transactionApi() {
        return GroupedOpenApi.builder()
                .group("transaction-api") // API grubu adı
                .pathsToMatch("/api/auth/transactions/**") // Transaction API'lerini dahil et
                .build();
    }
}
