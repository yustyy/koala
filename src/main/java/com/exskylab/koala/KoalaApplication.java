package com.exskylab.koala;

import com.exskylab.koala.core.properties.AppProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableConfigurationProperties(AppProperties.class)
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
public class KoalaApplication {

    public static void main(String[] args) {
        SpringApplication.run(KoalaApplication.class, args);
    }

}
