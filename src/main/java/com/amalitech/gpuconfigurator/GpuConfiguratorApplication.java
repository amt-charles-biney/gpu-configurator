package com.amalitech.gpuconfigurator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class GpuConfiguratorApplication {

	public static void main(String[] args) {
		SpringApplication.run(GpuConfiguratorApplication.class, args);
	}

}
