package com.exskylab.koala.core.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "iyzico")
@Getter
@Setter
public class IyzicoProperties {

    private String apiKey;

    private String secretKey;

    private String baseUrl;

}
