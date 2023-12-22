package xyz.aimcup.auth.feign.osu;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import xyz.aimcup.auth.feign.osu.model.OsuUserExtended;

@FeignClient(name = "osu-api",
        url = "https://osu.ppy.sh/api/v2/",
        configuration = FeignOsuClientConfiguration.class)
public interface FeignOsuClient {

    @GetMapping("/users/{id}/osu")
    OsuUserExtended getUserById(@PathVariable String id);
}
