package com.ep18.couriersync.backend;

import org.springframework.boot.SpringApplication;

public class TestCouriersyncBackendApplication {

	public static void main(String[] args) {
		SpringApplication.from(CouriersyncBackendApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
