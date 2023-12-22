package xyz.aimcup.auth.feign.osu.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OsuUser {
    private Long id;
    private String username;
    @JsonProperty("avatar_url")
    private String avatarUrl;

}
