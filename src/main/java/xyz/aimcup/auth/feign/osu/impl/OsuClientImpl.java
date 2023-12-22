package xyz.aimcup.auth.feign.osu.impl;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import xyz.aimcup.auth.feign.osu.FeignOsuClient;
import xyz.aimcup.auth.feign.osu.OsuClient;
import xyz.aimcup.auth.feign.osu.model.OsuUserExtended;

@Service
@RequiredArgsConstructor
public class OsuClientImpl implements OsuClient {
    private final FeignOsuClient osuClient;

    @Override
    public OsuUserExtended getUserById(String id) {
        try {
            return osuClient.getUserById(id);
        } catch (FeignException.NotFound e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User with id " + id + " not found");
        } catch (FeignException.BadRequest e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad request");
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "External API call failed");
        }
    }

}
