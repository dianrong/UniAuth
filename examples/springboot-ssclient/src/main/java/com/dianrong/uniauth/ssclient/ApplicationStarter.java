package com.dianrong.uniauth.ssclient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;

@EnableAutoConfiguration
@Configuration
@ComponentScan(basePackageClasses={ApplicationStarter.class})
@PropertySource(value = "classpath:/config/application.yml")
@ImportResource({"classpath:spring-context.xml"})
public class ApplicationStarter {
    public static void main(String[] args) {
        SpringApplication.run(ApplicationStarter.class, args);
    }
}
