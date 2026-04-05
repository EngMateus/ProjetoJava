package br.edu.faculdadedonaduzzi.dataservice;

import br.edu.faculdadedonaduzzi.dataservice.config.ModelConfiguration; // Import do seu Record
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties; // Import da ativação

@SpringBootApplication
@EnableConfigurationProperties(ModelConfiguration.class) // <-- O COMANDO DE ATIVAÇÃO AQUI
public class DataserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(DataserviceApplication.class, args);
	}

}
