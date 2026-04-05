package br.edu.faculdadedonaduzzi.dataservice.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "cleaning")
public record CleaningConfig(String agentName, String strategy) {
}