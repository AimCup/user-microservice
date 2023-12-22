package xyz.aimcup.auth.service;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.UserRepresentation;
import xyz.aimcup.auth.feign.osu.model.OsuUser;

import java.util.UUID;

public interface KeycloakService {
    Keycloak getKeycloak();

    UserRepresentation createUser(OsuUser osuUser);

    UUID createUser(UserRepresentation userRepresentation);

}
