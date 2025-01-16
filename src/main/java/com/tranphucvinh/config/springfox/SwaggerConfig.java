package com.tranphucvinh.config.springfox;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
@OpenAPIDefinition(info = @io.swagger.v3.oas.annotations.info.Info(title = "Admin APIs", version = "1.0", description = "Admin APIs"))
public class SwaggerConfig {

    @Bean
    public GroupedOpenApi publicAPI() {
        return GroupedOpenApi.builder().group("AuthService").packagesToScan("com.tranphucvinh.controller").build();
    }

    @Bean
    public OpenAPI customOpenAPI() {

        List<Server> servers = new ArrayList<>();
        return new OpenAPI().servers(servers)
                .components(
                        new Components().addSecuritySchemes("bearer-jwt",
                        new SecurityScheme()
                        .type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")
                        .in(SecurityScheme.In.HEADER).name("Authentication"))
                  )
                .addSecurityItem(new SecurityRequirement().addList("bearer-jwt", Arrays.asList("read", "write")))
                .info(
                        new Info()
                        .title("Authentication Rest API")
                        .description("Sample OpenAPI 3.0")
                        .contact(new Contact().name("Enjoyworks").url("tpvinh@enjoyworks.co.kr"))
                        .license(new License().name("Apache 2.0").url("http://www.apache.org/licenses/LICENSE-2.0.html"))
                        .version("1.0.0")
                 );
    }
}
