package br.edu.faculdadedonaduzzi.dataservice.dto;


public record SpamCheckResponse(String category, Double confidenceScore, boolean isBlocked) {
}