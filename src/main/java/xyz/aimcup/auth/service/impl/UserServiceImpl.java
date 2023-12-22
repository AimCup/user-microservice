package xyz.aimcup.auth.service.impl;

import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;
import xyz.aimcup.auth.data.entity.User;
import xyz.aimcup.auth.feign.osu.OsuClient;
import xyz.aimcup.auth.feign.osu.model.OsuUserExtended;
import xyz.aimcup.auth.mapper.user.UserMapper;
import xyz.aimcup.auth.properties.AimCupProperties;
import xyz.aimcup.auth.service.KeycloakService;
import xyz.aimcup.auth.service.UserService;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final KeycloakService keycloakService;
    private final OsuClient osuClient;
    private final UserMapper userMapper;
    private final AimCupProperties aimCupProperties;

    @Override
    public User getUserByOsuId(String osuId) {
        Keycloak keycloak = keycloakService.getKeycloak();
        return keycloak.realm(aimCupProperties.getKeycloak().getRealm())
                .users()
                .searchByUsername(osuId, true)
                .stream()
                .filter(u -> u.getUsername().equals(osuId))
                .findFirst()
                .map(userMapper::userRepresentationToUser)
                .orElseGet(() -> createUserByOsuId(osuId));
    }

    private User createUserByOsuId(String osuId) {
        OsuUserExtended osuUser = osuClient.getUserById(osuId);
        UserRepresentation userRepresentation = keycloakService.createUser(osuUser);
        return userMapper.userRepresentationToUser(userRepresentation);
    }
}
