package br.edu.faculdadedonaduzzi.dataservice;

import br.edu.faculdadedonaduzzi.dataservice.config.CleaningConfig; // Importe o novo Record
import br.edu.faculdadedonaduzzi.dataservice.config.ModelConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
// Adicione o CleaningConfig.class dentro das chaves { } para ativar ambos
@EnableConfigurationProperties({ModelConfiguration.class, CleaningConfig.class})
public class DataserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(DataserviceApplication.class, args);
	}

}