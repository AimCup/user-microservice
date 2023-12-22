package xyz.aimcup.auth.feign.osu;

import xyz.aimcup.auth.feign.osu.model.OsuUserExtended;

public interface OsuClient {
    OsuUserExtended getUserById(String id);

}
