package com.dianrong.uniauth.ssclient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

import com.dianrong.uniauth.ssclient.config.SSConfiguration;

@SpringBootApplication
@Import({ SSConfiguration.class })
public class ApplicationStarter {
	public static void main(String[] args) {
		SpringApplication.run(ApplicationStarter.class, args);
	}
}
