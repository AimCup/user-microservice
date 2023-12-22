package xyz.aimcup.auth.feign;

import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class FeignExceptionErrorDecoder  implements ErrorDecoder {
    private final ErrorDecoder errorDecoder = new Default();

    @Override
    public Exception decode(String s, Response response) {
        switch (response.status()) {
            case 400:
                return new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad request");
            case 404:
                return new ResponseStatusException(HttpStatus.NOT_FOUND, "User with provided id not found");
            default:
                return errorDecoder.decode(s, response);
        }
    }
}
