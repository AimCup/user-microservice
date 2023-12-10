package xyz.aimcup.auth.feign.osu;

import feign.RequestInterceptor;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import xyz.aimcup.auth.feign.osu.model.OsuToken;

@Log4j2
@RequiredArgsConstructor
class FeignOsuClientConfiguration {
    private final RestTemplate restTemplate;

    @Value("${osu.api.clientId}")
    private String clientId;

    @Value("${osu.api.clientSecret}")
    private String clientSecret;

    @Bean
    @Scope("prototype")
    public RequestInterceptor osuAccessTokenInterceptor() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("Accept", MediaType.APPLICATION_FORM_URLENCODED_VALUE);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("client_id", clientId);
        map.add("client_secret", clientSecret);
        map.add("grant_type", "client_credentials");
        map.add("scope", "public");
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(map, headers);
        ResponseEntity<OsuToken> response =
                restTemplate.exchange("https://osu.ppy.sh/oauth/token",
                        HttpMethod.POST,
                        entity,
                        OsuToken.class);
        if (response.getBody() == null) {
            log.error("Failed to get access token from osu api");
            throw new RuntimeException("Failed to get access token from osu api");
        }
        String accessToken = response.getBody().getAccessToken();
        return requestTemplate -> requestTemplate.header("Authorization", "Bearer " + accessToken);
    }
}
