package br.edu.faculdadedonaduzzi.dataservice.controller;

import br.edu.faculdadedonaduzzi.dataservice.dto.PredictionResponse;
import br.edu.faculdadedonaduzzi.dataservice.dto.SpamCheckResponse;
import br.edu.faculdadedonaduzzi.dataservice.dto.SpamCheckRequest;
import br.edu.faculdadedonaduzzi.dataservice.service.InferenceService;
import br.edu.faculdadedonaduzzi.dataservice.service.PredictionService;
import br.edu.faculdadedonaduzzi.dataservice.service.SpamDetectorService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class DataController {

    private final PredictionService predictionService;
    private final InferenceService inferenceService;
    private final SpamDetectorService spamDetectorService; // Adicionado


    public DataController(PredictionService ps, InferenceService is, SpamDetectorService sds) {
        this.predictionService = ps;
        this.inferenceService = is;
        this.spamDetectorService = sds;
    }

    @GetMapping("/status")
    public String checkStatus() {
        return inferenceService.getModelInfo();
    }

    @GetMapping("/predict")
    public PredictionResponse getPrediction(@RequestParam("id") String customerId,
                                            @RequestParam("amount") double amount) {
        double score = predictionService.calculateFraudRisk(customerId, amount);
        return new PredictionResponse(customerId, score);
    }


    @PostMapping("/classify")
    public SpamCheckResponse classifyMessage(@RequestBody SpamCheckRequest request) {

        return spamDetectorService.analyze(request.messageContent());
    }
}