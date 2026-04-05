package br.edu.faculdadedonaduzzi.dataservice.service;

import org.springframework.stereotype.Service;
import java.io.BufferedReader;
import java.io.InputStreamReader;

@Service
public class FishOrchestratorService {

    public String analyzeWithPython(String imagePath) {
        try {
            // 1. ProcessBuilder: O Java abre o terminal de forma invisível
            // O python interpreter e o script_path devem estar no ambiente
            ProcessBuilder pb = new ProcessBuilder("python", "predict.py", imagePath);

            // 2. Fundamental: Unifica stdout e stderr para capturar erros de bibliotecas (ex: cv2)
            pb.redirectErrorStream(true);

            Process process = pb.start();

            // 3. Lendo a Resposta: O Java lê o console do Python linha por linha
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder output = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                output.append(line);
            }

            // 4. waitFor: Pausa a Virtual Thread do Java 21 até o Python terminar
            int exitCode = process.waitFor();

            // 5. Gestão de Erros: Traduzimos códigos do SO para mensagens compreensíveis
            if (exitCode != 0) {
                return "Erro no script Python: " + output.toString();
            }

            String result = output.toString().trim();

            // Validação do contrato: O script deve responder apenas FRESCO ou ESTRAGADO
            if ("FRESCO".equalsIgnoreCase(result) || "ESTRAGADO".equalsIgnoreCase(result)) {
                return result.toUpperCase();
            } else {
                return "Resposta inesperada do script: " + result;
            }

        } catch (Exception e) {
            return "Falha na orquestração: " + e.getMessage();
        }
    }
}