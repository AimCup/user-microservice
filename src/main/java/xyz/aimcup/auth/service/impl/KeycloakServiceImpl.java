package xyz.aimcup.auth.service.impl;

import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;
import xyz.aimcup.auth.feign.osu.model.OsuUser;
import xyz.aimcup.auth.properties.AimCupProperties;
import xyz.aimcup.auth.properties.KeycloakProperties;
import xyz.aimcup.auth.service.KeycloakService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class KeycloakServiceImpl implements KeycloakService {
    private final AimCupProperties aimCupProperties;

    public Keycloak getKeycloak() {
        KeycloakProperties keycloakProperties = aimCupProperties.getKeycloak();
        return KeycloakBuilder.builder()
                .serverUrl(keycloakProperties.getServerUrl())
                .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                .realm(keycloakProperties.getRealm())
                .clientId(keycloakProperties.getClientId())
                .clientSecret(keycloakProperties.getClientSecret())
                .build();
    }

    @Override
    public UserRepresentation createUser(OsuUser osuUser) {
        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setUsername(osuUser.getId().toString());
        userRepresentation.setEnabled(true);
        Map<String, List<String>> attributes = new HashMap<>();
        attributes.put("username", List.of(osuUser.getUsername()));
        userRepresentation.setAttributes(attributes);
        UUID userId = this.createUser(userRepresentation);
        userRepresentation.setId(userId.toString());
        return userRepresentation;
    }

    @Override
    public UUID createUser(UserRepresentation userRepresentation) {
        KeycloakProperties keycloakProperties = aimCupProperties.getKeycloak();
        try (Response response =
                     this.getKeycloak()
                             .realm(keycloakProperties.getRealm())
                             .users()
                             .create(userRepresentation)) {
            return UUID.fromString(response.getLocation().getPath().replaceAll(".*/([^/]+)$", "$1"));
        }
    }

}
