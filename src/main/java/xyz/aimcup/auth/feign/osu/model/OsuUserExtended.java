package xyz.aimcup.auth.feign.osu.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OsuUserExtended extends OsuUser {

    @JsonProperty("cover_url")
    private String coverUrl;

    @JsonProperty("discord")
    private String discord;

    @JsonProperty("has_supported")
    private Boolean hasSupported;

    @JsonProperty("interests")
    private String interests;

    @JsonProperty("join_date")
    private String joinDate;

    @JsonProperty("location")
    private String location;

    @JsonProperty("title")
    private String title;
}
