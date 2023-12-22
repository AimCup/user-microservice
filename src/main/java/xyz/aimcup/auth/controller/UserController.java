package xyz.aimcup.auth.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.RestController;
import xyz.aimcup.auth.data.entity.User;
import xyz.aimcup.auth.mapper.user.UserMapper;
import xyz.aimcup.auth.service.UserService;
import xyz.aimcup.generated.UserApi;
import xyz.aimcup.generated.model.UserResponseDTO;

@RestController
@RequiredArgsConstructor
public class UserController implements UserApi {
    private final UserService userService;
    private final UserMapper userMapper;

    @Secured("OIDC_USER")
    @Override
    public ResponseEntity<UserResponseDTO> getUserByOsuId(String osuId) {
        User user = userService.getUserByOsuId(osuId);
        UserResponseDTO userResponseDTO = userMapper.userToUserResponseDto(user);
        return ResponseEntity.ok(userResponseDTO);
    }

    // TODO: AC-61
    @Override
    public ResponseEntity<UserResponseDTO> me() {
        return null;
    }

}
