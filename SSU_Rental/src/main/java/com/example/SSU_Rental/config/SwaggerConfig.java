package com.example.SSU_Rental.config;

import com.example.SSU_Rental.member.AuthMember;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import java.util.ArrayList;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class SwaggerConfig {
    private String version = "V0.1";

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .components(new Components())
            .info(new Info().title("SSU RENTAL API")
                .description("SSU RENTAL API 명세서 입니다.")
                .version("v0.0.1"));
    }




}
