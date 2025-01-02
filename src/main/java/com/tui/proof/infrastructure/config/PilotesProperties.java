package com.tui.proof.infrastructure.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Data
@Configuration
@ConfigurationProperties(prefix = "pilotes")
public class PilotesProperties {

    private double pricePerPilotes = 1.33;
    private int orderTimeWindow = 1;
}
