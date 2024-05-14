package kr.tgwing.tech.common.swagger;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

@OpenAPIDefinition(info = @Info(title = "tg-wing 홈페이지", description = "다들 아자아자 화이팅^^", version = "v1"))

@Configuration
public class SwaggerConfig {

    @Value("${custom.swagger.server}")
    private String swaggerServer;

    @Value("${server.servlet.context-path}")
    private String context;

    @Bean
    public OpenAPI openAPI() {
        SecurityScheme securityScheme = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")
                .in(SecurityScheme.In.HEADER).name("Authorization");
        SecurityRequirement securityRequirement = new SecurityRequirement().addList("bearerAuth");

        Server serverDev = new Server();
        serverDev.setDescription("dev");
        serverDev.setUrl("http://" + swaggerServer + context);
        return new OpenAPI()
                .components(new Components().addSecuritySchemes("bearerAuth", securityScheme))
                .security(Arrays.asList(securityRequirement))
                .servers(List.of(serverDev));
    }

}
