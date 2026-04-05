package br.edu.faculdadedonaduzzi.dataservice.service;

import org.springframework.stereotype.Service;
import java.io.BufferedReader;
import java.io.InputStreamReader;

@Service
public class FishOrchestratorService {

    public String analyzeWithPython(String imagePath) {
        try {
            // No Docker, usamos o comando nativo do Linux instalado no Dockerfile
            // O caminho do script permanece relativo à raiz do container (/app)
            ProcessBuilder pb = new ProcessBuilder(
                    "python3.13",
                    "dataservice/predict.py",
                    imagePath
            );

            pb.redirectErrorStream(true);
            Process process = pb.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder output = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                output.append(line);
            }

            int exitCode = process.waitFor();

            if (exitCode != 0) {
                return "Erro no script Python: " + output.toString();
            }

            String result = output.toString().trim();

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