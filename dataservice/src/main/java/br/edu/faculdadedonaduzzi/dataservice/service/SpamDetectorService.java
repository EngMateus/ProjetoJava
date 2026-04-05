package br.edu.faculdadedonaduzzi.dataservice.service;

import br.edu.faculdadedonaduzzi.dataservice.dto.SpamCheckResponse; // Importando seu record (com o nome do slide)
import org.springframework.stereotype.Service;

@Service
public class SpamDetectorService {

    public SpamCheckResponse analyze(String text) {
        // Se o texto for nulo, retornamos um estado de erro seguro
        if (text == null) return new SpamCheckResponse("ERRO", 0.0, false);

        String lower = text.toLowerCase();
        double score = 0.0;

        // "Modelo" Heurístico: Simulando pesos de uma IA de elite
        // Cada palavra "perigosa" aumenta a pontuação de risco
        if (lower.contains("grátis")) score += 0.4;
        if (lower.contains("dinheiro")) score += 0.3;
        if (lower.contains("clique aqui")) score += 0.5;
        if (lower.contains("promoção")) score += 0.2;

        // Normalização: O score final deve ficar entre 0 e 1 (0% a 100%)
        score = Math.min(score, 1.0);

        // Regra de Negócio: Se a pontuação for maior que 0.6 (60%), bloqueamos
        boolean isSpam = score > 0.6;

        return new SpamCheckResponse(
                isSpam ? "SPAM" : "HAM", // HAM é o jargão técnico para mensagens legítimas
                score,
                isSpam
        );
    }
}