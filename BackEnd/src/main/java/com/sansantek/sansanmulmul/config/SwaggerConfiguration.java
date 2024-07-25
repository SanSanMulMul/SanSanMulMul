package com.sansantek.sansanmulmul.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class SwaggerConfiguration {
    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder().group("all").pathsToMatch("/**").build();
    }

    @Bean
    public GroupedOpenApi userApi() {
        return GroupedOpenApi.builder().group("user").pathsToMatch("/user/**").build();
    }

    @Bean
    public GroupedOpenApi mountainApi() {
        return GroupedOpenApi.builder().group("mountain").pathsToMatch("/mountain/**").build();
    }

}
