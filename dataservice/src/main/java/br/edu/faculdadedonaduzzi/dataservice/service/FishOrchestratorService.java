package br.edu.faculdadedonaduzzi.dataservice.service;

import org.springframework.stereotype.Service;
import java.io.BufferedReader;
import java.io.InputStreamReader;

@Service
public class FishOrchestratorService {

    public String analyzeWithPython(String imagePath) {
        try {
            // Monta o comando para executar o Python no terminal do servidor
            ProcessBuilder pb = new ProcessBuilder("python", "predict.py", imagePath);
            pb.redirectErrorStream(true);

            Process process = pb.start();

            // Lê a resposta do Python (Processador Matemático)
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder output = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                output.append(line);
            }

            int exitCode = process.waitFor();
            return exitCode == 0 ? output.toString() : "Erro no script Python";

        } catch (Exception e) {
            return "Falha na orquestração: " + e.getMessage();
        }
    }
}