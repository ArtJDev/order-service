package ru.itbooks.orderservice.config;

import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.net.URI;

@ConfigurationProperties(prefix = "itbooks")
public record ClientProperties(
        @NotNull
        URI productServiceUri
) {
}
