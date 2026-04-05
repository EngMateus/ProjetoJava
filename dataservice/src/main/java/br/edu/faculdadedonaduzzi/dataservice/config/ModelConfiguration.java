package br.edu.faculdadedonaduzzi.dataservice.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app")
public record ModelConfiguration(ModelData model) {


    public record ModelData(
            double defaultThreshold,
            String version,
            String path
    ) {}
}