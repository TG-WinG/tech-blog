package kr.tgwing.tech.common.swagger;

import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;

@OpenAPIDefinition(info = @Info(title = "tg-wing 홈페이지", description = "다들 아자아자 화이팅^^", version = "v1"))

@Configuration
@Profile({"dev", "default"})
public class SwaggerConfig {

    @Bean
    @Profile("dev")
    public Server devServer() {
        Server devServer = new Server();
        devServer.setDescription("dev");
        devServer.setUrl("http://ec2-43-200-221-178.ap-northeast-2.compute.amazonaws.com/api");
        return devServer;
    }

    @Bean
    @Profile("default")
    public Server defaultServer() {
        return null;
    }

    @Bean
    public OpenAPI openAPI(Server server) {
        SecurityScheme securityScheme = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")
                .in(SecurityScheme.In.HEADER).name("Authorization");
        SecurityRequirement securityRequirement = new SecurityRequirement().addList("bearerAuth");

        OpenAPI openAPI = new OpenAPI()
                .components(new Components().addSecuritySchemes("bearerAuth", securityScheme))
                .security(Arrays.asList(securityRequirement));
        if (server != null) {
            openAPI.servers(List.of(server));
        }

        return openAPI;
    }

}
