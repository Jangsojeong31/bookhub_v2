package com.bookhub.bookhub_back.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    // 사원 관련 api
    @Bean
    public GroupedOpenApi employeeApi() {
        return GroupedOpenApi.builder()
                .group("employee-api")
                .pathsToMatch(
                        "/api/v1/admin/branches/**",
                        "/api/v1/auth/branches/**",
                        "/api/v1/admin/employees/**",
                        "/api/v1/auth/positions/**",
                        "/api/v1/auth/authorities/**"
                )
                .build();
    }

    // 발주 관련 api
    @Bean
    public GroupedOpenApi orderApi() {
        return GroupedOpenApi.builder()
                .group("order-api")
                .pathsToMatch(
                        "/api/v1/admin/purchase-orders/**",
                        "/api/v1/manager/purchase-orders/**",
                        "/api/v1/admin/purchase-order-approvals/**",
                        "/api/v1/admin/receptions/**",
                        "/api/v1/manager/receptions/**"
                        )
                .build();
    }

    // 도서 관련 api
    @Bean
    public GroupedOpenApi bookApi() {
        return GroupedOpenApi.builder()
                .group("book-api")
                .pathsToMatch(
                        "/api/v1/admin/publishers/**",
                        "/api/v1/common/policies/**",
                        "/api/v1/admin/policies/**",
                        "/api/v1/admin/book-logs/**",
                        "/api/v1/manager/locations/**",
                        "/api/v1/common/locations/**",
                        "/api/v1/admin/books/**",
                        "/api/v1/common/books/**",
                        "/api/v1/admin/categories/**",
                        "/api/v1/common/categories/**",
                        "/api/v1/admin/authors/**"
                )
                .build();
    }

    // 회원관리 관련 api
    @Bean
    public GroupedOpenApi managementApi() {
        return GroupedOpenApi.builder()
                .group("employee-management-api")
                .pathsToMatch(
                        "/api/v1/auth/signup",
                        "/api/v1/auth/login",
                        "/api/v1/auth/users/**",
                        "/api/v1/auth/logout",
                        "/api/v1/admin/employee-change-logs",
                        "/api/v1/admin/employee-exit-logs",
                        "/api/v1/admin/employee-signup-approvals"
                )
                .build();
    }
    // 재고 관련 api
    @Bean
    public GroupedOpenApi stockApi() {
        return GroupedOpenApi.builder()
                .group("stock-api")
                .pathsToMatch(
                        "/api/v1/admin/stocks/**",
                        "/api/v1/manager/stocks/**",
                        "/api/v1/admin/stock-logs",
                        "/api/v1/manager/stock-logs"
                )
                .build();
    }

    // 통계 관련 api
    @Bean
    public GroupedOpenApi statisticsApi() {
        return GroupedOpenApi.builder()
                .group("statistics-api")
                .pathsToMatch(
                        "/api/v1/admin/statistics/**",
                        "/api/v1/common/statistics/**"
                )
                .build();
    }

    // 알림 관련 api
    @Bean
    public GroupedOpenApi alertApi() {
        return GroupedOpenApi.builder()
                .group("alert-api")
                .pathsToMatch(
                        "/api/v1/common/alerts/**"
                )
                .build();
    }

    // 이메일 관련 api
    @Bean
    public GroupedOpenApi mailApi() {
        return GroupedOpenApi.builder()
                .group("mail-api")
                .pathsToMatch(
                        "/api/v1/auth/emails/**"
                )
                .build();
    }

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("BookHub API")
                        .description("도서 관리 시스템 API 문서입니다.")
                        .version("v1.0"));

    }
}
