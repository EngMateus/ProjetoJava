package br.edu.faculdadedonaduzzi.dataservice.controller;

import br.edu.faculdadedonaduzzi.dataservice.service.CleanerService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/cleaner")
public class CleanerController {

    private final CleanerService cleanerService;

    public CleanerController(CleanerService cleanerService) {
        this.cleanerService = cleanerService;
    }

    @GetMapping("/info")
    public String getInfo(@RequestParam(value = "user", defaultValue = "Guest") String user) {
        return "Olá " + user + "! " + cleanerService.getModelInfo();
    }
}