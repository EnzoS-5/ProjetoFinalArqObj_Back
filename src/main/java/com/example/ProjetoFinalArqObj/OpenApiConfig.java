package com.example.ProjetoFinalArqObj;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI projetoFinalOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Projeto Final ArqObj API")
                        .version("v1")
                        .description("Documentação da API do projeto final."));
    }
}
