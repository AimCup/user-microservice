package xyz.aimcup.auth.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "api")
@Data
public class OsuApiProperties {
    private String clientId;
    private String clientSecret;
}
