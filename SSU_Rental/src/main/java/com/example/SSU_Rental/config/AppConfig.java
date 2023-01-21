package com.example.SSU_Rental.config;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@ConfigurationProperties(prefix = "SSURental")
public class AppConfig {

    public String key;

}
