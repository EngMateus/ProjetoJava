package br.edu.faculdadedonaduzzi.dataservice.dto;

public class PredictionResponse {
    private String customerId;
    private double riskScore;
    private String status;

    public PredictionResponse(String customerId, double riskScore) {
        this.customerId = customerId;
        this.riskScore = riskScore;

        this.status = riskScore > 0.7 ? "ALTO_RISCO" : "SEGURO";
    }


    public String getCustomerId() { return customerId; }
    public double getRiskScore() { return riskScore; }
    public String getStatus() { return status; }
}