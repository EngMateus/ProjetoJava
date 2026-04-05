package br.edu.faculdadedonaduzzi.dataservice.service;

import br.edu.faculdadedonaduzzi.dataservice.config.ModelConfiguration;
import org.springframework.stereotype.Service;

@Service
public class InferenceService {

    private final ModelConfiguration modelConfiguration;


    public InferenceService(ModelConfiguration modelConfiguration) {
        this.modelConfiguration = modelConfiguration;
    }

    public String getModelInfo() {

        return String.format(
                "Sistema operando com o modelo %s [Threshold: %.2f] localizado em: %s",
                modelConfiguration.model().version(),
                modelConfiguration.model().defaultThreshold(),
                modelConfiguration.model().path()
        );
    }
}