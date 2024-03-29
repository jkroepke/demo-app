package io.github.jkroepke.demo;

import com.azure.spring.cloud.autoconfigure.implementation.context.properties.AzureGlobalProperties;
import io.github.jkroepke.demo.fileupload.properties.StorageProperties;
import io.github.jkroepke.demo.fileupload.service.StorageService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
public class DemoApplication {
	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

    @Bean
    CommandLineRunner init(StorageService storageService) {
        return (args) -> {
            storageService.deleteAll();
            storageService.init();
        };
    }

    // https://github.com/Azure/azure-sdk-for-java/issues/36377
    @Bean
    public AzureGlobalProperties azureGlobalProperties() {
        return new AzureGlobalProperties();
    }
}
