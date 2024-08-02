package com.sansantek.sansanmulmul.config.swagger;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfiguration {

    @Bean
    public OpenAPI openAPI() {
        // Define the security scheme
        SecurityScheme securityScheme = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .in(SecurityScheme.In.HEADER)
                .name("Authorization");

        // Define the security requirement
        SecurityRequirement securityRequirement = new SecurityRequirement()
                .addList("bearerAuth");

        return new OpenAPI()
                .addServersItem(new Server().url("https://i11d111.p.ssafy.io"))
                .components(new Components().addSecuritySchemes("bearerAuth", securityScheme))
                .addSecurityItem(securityRequirement)
                .info(apiInfo());
    }

    private Info apiInfo() {
        return new Info()
                .title("산산물물 API 문서")
                .description("산산물물 API에 대한 문서입니다.")
                .version("1.0.0");
    }


    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("all")
                .pathsToMatch("/**")
                .build();
    }

    @Bean
    public GroupedOpenApi userApi() {
        return GroupedOpenApi.builder()
                .group("user")
                .pathsToMatch("/user/**")
                .build();
    }

    @Bean
    public GroupedOpenApi mountainApi() {
        return GroupedOpenApi.builder()
                .group("mountain")
                .pathsToMatch("/mountain/**")
                .build();
    }

    @Bean
    public GroupedOpenApi crewApi() {
        return GroupedOpenApi.builder()
                .group("crew")
                .pathsToMatch("/crew/**")
                .build();
    }
}
