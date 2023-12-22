package xyz.aimcup.auth.mapper.user;

import org.keycloak.representations.idm.UserRepresentation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import xyz.aimcup.auth.data.entity.User;
import xyz.aimcup.generated.model.UserResponseDTO;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponseDTO userToUserResponseDto(User user);


    @Mapping(target = "osuId", source = "username")
    @Mapping(target = "username", source = "username")
    @Mapping(target = "isRestricted", source = "enabled")
    User userRepresentationToUser(UserRepresentation userRepresentation);
}
