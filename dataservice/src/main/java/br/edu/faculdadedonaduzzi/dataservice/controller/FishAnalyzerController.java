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

    @PostMapping(value = "/classify-python", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> classificar(@RequestPart("imagem") MultipartFile file) {
        Path tempFile = null; // Declarado fora para ser acessível no finally
        try {
            // Importância do createTempFile: Unicidade e uso do diretório /tmp do SO
            tempFile = Files.createTempFile("fish_", ".jpg");
            file.transferTo(tempFile.toFile());

            // Chamar o script Python com a imagem (O Java como Orquestrador)
            String resultado = orchestratorService.analyzeWithPython(tempFile.toAbsolutePath().toString());

            return ResponseEntity.ok(resultado);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erro no processamento: " + e.getMessage());
        } finally {
            // REGRA DE OURO: O uso do finally garante a exclusão mesmo em caso de erro
            if (tempFile != null) {
                try {
                    Files.deleteIfExists(tempFile);
                } catch (Exception ignored) {
                    // Apenas para evitar que erros na limpeza travem a resposta
                }
            }
        }
    }
}