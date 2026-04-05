package br.edu.faculdadedonaduzzi.dataservice.service;

import br.edu.faculdadedonaduzzi.dataservice.config.CleaningConfig;
import org.springframework.stereotype.Service;

@Service
public class CleanerService {

    private final CleaningConfig cleaningConfig;

    public CleanerService(CleaningConfig cleaningConfig) {
        this.cleaningConfig = cleaningConfig;
    }

    public String getModelInfo() {
        return String.format("Agente: %s | Estratégia: %s",
                cleaningConfig.agentName(),
                cleaningConfig.strategy());
    }
}