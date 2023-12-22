package xyz.aimcup.auth.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "aimcup")
@Data
public class AimCupProperties {
    private KeycloakProperties keycloak;
}
