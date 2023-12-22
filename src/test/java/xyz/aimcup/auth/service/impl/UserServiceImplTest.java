package xyz.aimcup.auth.service.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.UserRepresentation;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import xyz.aimcup.auth.data.entity.User;
import xyz.aimcup.auth.feign.osu.OsuClient;
import xyz.aimcup.auth.feign.osu.model.OsuUserExtended;
import xyz.aimcup.auth.mapper.user.UserMapper;
import xyz.aimcup.auth.properties.AimCupProperties;
import xyz.aimcup.auth.properties.KeycloakProperties;
import xyz.aimcup.auth.service.KeycloakService;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(properties = {
        "aimcup.keycloak.realm=aimcup_realm"
},
        classes = UserServiceImpl.class)
class UserServiceImplTest {

    @MockBean
    private KeycloakService keycloakService;

    @MockBean
    private OsuClient osuClient;

    @MockBean
    private UserMapper userMapper;

    @MockBean
    private AimCupProperties aimCupProperties;

    @Autowired
    private UserServiceImpl userService;

    @Test
    @DisplayName("Should return User when user exists in Keycloak")
    void shouldReturnUserWhenUserExistsInKeycloak() {
        // given
        final String osuId = "3660794";
        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setId(osuId);
        userRepresentation.setUsername(osuId);

        final Keycloak keycloak = mock(Keycloak.class);
        RealmResource resource = mock(RealmResource.class);
        UsersResource usersResource = mock(UsersResource.class);
        KeycloakProperties keycloakProperties = mock(KeycloakProperties.class);

        final User user = User.builder()
                .id(UUID.randomUUID())
                .osuId(osuId)
                .build();

        // when
        when(aimCupProperties.getKeycloak()).thenReturn(keycloakProperties);
        when(keycloakProperties.getRealm()).thenReturn("aimcup_realm");
        when(keycloakService.getKeycloak()).thenReturn(keycloak);
        when(keycloak.realm("aimcup_realm")).thenReturn(resource);
        when(resource.users()).thenReturn(usersResource);
        when(usersResource.searchByUsername(osuId, true)).thenReturn(List.of(userRepresentation));
        when(userMapper.userRepresentationToUser(userRepresentation)).thenReturn(user);

        User expectedUser = userService.getUserByOsuId(osuId);

        // then
        assertThat(expectedUser.getOsuId()).isEqualTo(user.getOsuId());
    }

    @Test
    @DisplayName("Should create User when user does not exist in Keycloak")
    void shouldCreateUserWhenUserDoesNotExistInKeycloak() {
        // given
        final String osuId = "3660794";
        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setId(osuId);
        userRepresentation.setUsername(osuId);

        final Keycloak keycloak = mock(Keycloak.class);
        RealmResource resource = mock(RealmResource.class);
        UsersResource usersResource = mock(UsersResource.class);
        OsuUserExtended osuUserExtended = mock(OsuUserExtended.class);
        KeycloakProperties keycloakProperties = mock(KeycloakProperties.class);

        final User user = User.builder()
                .id(UUID.randomUUID())
                .osuId(osuId)
                .build();

        // when
        when(aimCupProperties.getKeycloak()).thenReturn(keycloakProperties);
        when(keycloakProperties.getRealm()).thenReturn("aimcup_realm");
        when(keycloakService.getKeycloak()).thenReturn(keycloak);
        when(keycloak.realm("aimcup_realm")).thenReturn(resource);
        when(resource.users()).thenReturn(usersResource);
        when(usersResource.searchByUsername(osuId, true)).thenReturn(List.of());

        when(osuClient.getUserById(osuId)).thenReturn(osuUserExtended);
        when(keycloakService.createUser(osuUserExtended)).thenReturn(userRepresentation);

        when(userMapper.userRepresentationToUser(userRepresentation)).thenReturn(user);

        User expectedUser = userService.getUserByOsuId(osuId);

        // then
        assertThat(expectedUser.getOsuId()).isEqualTo(user.getOsuId());
    }

}