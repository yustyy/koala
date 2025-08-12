package com.exskylab.koala.core.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "sms.iletimerkezi")
@Getter
@Setter
public class IletiMerkeziProperties {
    private String apiKey;
    private String apiHash;
    private String sender;
}
