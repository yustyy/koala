package com.exskylab.koala.core.configs;

import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "app")
public record AppProperties (
    @NotBlank String apiUrl,
    @NotBlank String frontendUrl
) {}
