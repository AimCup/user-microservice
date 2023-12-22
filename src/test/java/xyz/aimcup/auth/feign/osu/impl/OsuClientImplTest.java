package xyz.aimcup.auth.feign.osu.impl;

import feign.FeignException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import xyz.aimcup.auth.feign.osu.FeignOsuClient;
import xyz.aimcup.auth.feign.osu.model.OsuUserExtended;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
class OsuClientImplTest {

    @Mock
    private FeignOsuClient feignOsuClient;

    @InjectMocks
    private OsuClientImpl osuClient;

    @Test
    @DisplayName("Should return user by id from osu! API")
    void shouldReturnUserByIdFromOsuApi() {
        // given
        final String id = "3660794";

        OsuUserExtended osuUserExtended = new OsuUserExtended();
        osuUserExtended.setId(Long.valueOf(id));

        // when
        Mockito.when(feignOsuClient.getUserById(id)).thenReturn(osuUserExtended);
        OsuUserExtended expectedUser = osuClient.getUserById(id);

        // then
        assertThat(expectedUser.getId()).isEqualTo(osuUserExtended.getId());
    }

    @Test
    @DisplayName("Should throw exception when user not found")
    void shouldThrowExceptionWhenUserNotFound() {
        // given
        final String id = "3660794";

        final FeignException.NotFound feignExceptionNotFound = Mockito.mock(FeignException.NotFound.class);

        // when
        Mockito.when(feignOsuClient.getUserById(id)).thenThrow(feignExceptionNotFound);

        // then
        assertThatThrownBy(() -> osuClient.getUserById(id))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("User with id " + id + " not found")
                .hasFieldOrPropertyWithValue("status", HttpStatus.NOT_FOUND);
    }

    @Test
    @DisplayName("Should throw exception when bad request")
    void shouldThrowExceptionWhenBadRequest() {
        // given
        final String id = "3660794";

        final FeignException.BadRequest feignExceptionBadRequest = Mockito.mock(FeignException.BadRequest.class);

        // when
        Mockito.when(feignOsuClient.getUserById(id)).thenThrow(feignExceptionBadRequest);

        // then
        assertThatThrownBy(() -> osuClient.getUserById(id))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Bad request")
                .hasFieldOrPropertyWithValue("status", HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("Should throw exception when external API call failed")
    void shouldThrowExceptionWhenExternalApiCallFailed() {
        // given
        final String id = "3660794";

        final FeignException feignException = Mockito.mock(FeignException.class);

        // when
        Mockito.when(feignOsuClient.getUserById(id)).thenThrow(feignException);

        // then
        assertThatThrownBy(() -> osuClient.getUserById(id))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("External API call failed")
                .hasFieldOrPropertyWithValue("status", HttpStatus.INTERNAL_SERVER_ERROR);
    }

}