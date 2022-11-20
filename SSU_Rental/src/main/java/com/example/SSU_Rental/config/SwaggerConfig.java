package com.example.SSU_Rental.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {


    @Bean
    public OpenAPI customOpenAPI(){
        return new OpenAPI()
            .components(new Components())
            .info(new Info().title("SSU RENTAL API")
                .description("SSU RENTAL API 명세서 입니다.")
                .version("v0.0.1"));
    }

}
