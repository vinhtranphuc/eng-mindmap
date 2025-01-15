package com.tranphucvinh;

import java.util.TimeZone;

import javax.annotation.PostConstruct;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.tranphucvinh.config.security.AuthProperties;

@SpringBootApplication
@EntityScan(basePackageClasses = { AdminApplication.class, })
@EnableConfigurationProperties(AuthProperties.class)
@EnableScheduling
public class AdminApplication {

    // TIMEZONE
    @Value("${server.timezone}")
    protected String severTimezone;

    @PostConstruct
    public void init() {
        TimeZone.setDefault(TimeZone.getTimeZone(severTimezone));
    }

    public static void main(String[] args) {
        SpringApplication.run(AdminApplication.class, args);
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
