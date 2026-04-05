package br.edu.faculdadedonaduzzi.dataservice.controller;

import br.edu.faculdadedonaduzzi.dataservice.service.FishOrchestratorService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;

@RestController
@RequestMapping("/api/v1/fish")
public class FishAnalyzerController {

    private final FishOrchestratorService orchestratorService;

    public FishAnalyzerController(FishOrchestratorService orchestratorService) {
        this.orchestratorService = orchestratorService;
    }

    // A adição do 'consumes' força o Swagger a mostrar o botão de upload de arquivo
    @PostMapping(value = "/classify-python", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> classificar(@RequestPart("imagem") MultipartFile file) {
        try {
            // Salva a imagem recebida em um arquivo temporário no Sistema Operacional
            Path tempFile = Files.createTempFile("fish_", ".jpg");
            file.transferTo(tempFile.toFile());

            // O Java atua como Orquestrador enviando o caminho para o Python
            String resultado = orchestratorService.analyzeWithPython(tempFile.toAbsolutePath().toString());

            // Remove o arquivo temporário (Meio de Campo) após a resposta do Python
            Files.deleteIfExists(tempFile);

            return ResponseEntity.ok(resultado);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erro no processamento: " + e.getMessage());
        }
    }
}