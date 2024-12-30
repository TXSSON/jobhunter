package vn.sondev.jobhunter1.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import vn.sondev.jobhunter1.domain.User;
import vn.sondev.jobhunter1.domain.request.UserCreationRequest;
import vn.sondev.jobhunter1.domain.request.UserUpdateRequest;
import vn.sondev.jobhunter1.domain.response.UserCreationResponse;
import vn.sondev.jobhunter1.domain.response.UserFetchResponse;
import vn.sondev.jobhunter1.domain.response.UserUpdateResponse;


@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {
    User toUser(UserCreationRequest userCreationRequest);
    UserUpdateResponse formUserToUserResponse(User user);
    UserCreationResponse toUserResponse(User user);
    UserFetchResponse toUserFetchRequest(User user);
    void updateUser (@MappingTarget User user, UserUpdateRequest userUpdateRequest);

}
