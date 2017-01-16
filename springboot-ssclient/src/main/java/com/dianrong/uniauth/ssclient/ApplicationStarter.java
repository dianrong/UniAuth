package com.dianrong.uniauth.ssclient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages="com.dianrong")
public class ApplicationStarter {
	public static void main(String[] args) {
		SpringApplication.run(ApplicationStarter.class, args);
	}
}
