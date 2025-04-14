package com.example.demo;

import org.springframework.boot.SpringApplication;

public class TestOoadApplication {

	public static void main(String[] args) {
		SpringApplication.from(OoadApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
