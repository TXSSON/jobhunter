package vn.sondev.jobhunter1.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import vn.sondev.jobhunter1.domain.User;
import vn.sondev.jobhunter1.domain.request.UserCreationRequest;
import vn.sondev.jobhunter1.domain.request.UserCriteriaRequest;
import vn.sondev.jobhunter1.domain.request.UserUpdateRequest;
import vn.sondev.jobhunter1.domain.response.UserCreationResponse;
import vn.sondev.jobhunter1.domain.response.UserFetchResponse;
import vn.sondev.jobhunter1.domain.response.UserUpdateResponse;
import vn.sondev.jobhunter1.exception.exceptioncustom.AppException;
import vn.sondev.jobhunter1.repository.UserRepository;
import vn.sondev.jobhunter1.service.mapper.UserMapper;
import vn.sondev.jobhunter1.service.specification.SpecificationBuilder;
import vn.sondev.jobhunter1.util.enums.ErrorCodeEnum;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {

    UserRepository userRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;
    SpecificationBuilder<User> specificationBuilder;

    public List<UserFetchResponse> fetchAllUserWithConditions(UserCriteriaRequest userCriteriaRequest, Pageable pageable) {
        if (userCriteriaRequest == null) {
            List<User> userList = this.userRepository.findAll(pageable).getContent();
            return userList.stream().map(userMapper::toUserFetchRequest).collect(Collectors.toList());
        }
        Specification<User> combinedSpec= Specification.where(null);
        if(userCriteriaRequest.getName() != null && !userCriteriaRequest.getName().isEmpty()) {
            Specification<User> nameSpec = this.specificationBuilder.whereNameLike("name", userCriteriaRequest.getName());
            combinedSpec = combinedSpec.and(nameSpec);
        }
        if(userCriteriaRequest.getAge() != null) {
            Specification<User> ageSpec = this.specificationBuilder.whereNameEqual("age", userCriteriaRequest.getAge());
            combinedSpec = combinedSpec.and(ageSpec);
        }
        if(userCriteriaRequest.getEmail() != null && !userCriteriaRequest.getEmail().isEmpty()) {
            Specification<User> emailSpec = this.specificationBuilder.whereNameLike("email", userCriteriaRequest.getEmail());
            combinedSpec = combinedSpec.and(emailSpec);
        }
        if (userCriteriaRequest.getAddress() != null && !userCriteriaRequest.getAddress().isEmpty()) {
            Specification<User> addressSpec = this.specificationBuilder.whereNameLike("address", userCriteriaRequest.getAddress());
            combinedSpec = combinedSpec.and(addressSpec);
        }
        if (userCriteriaRequest.getGender() != null) {
            Specification<User> genderSpec = this.specificationBuilder.whereNameLike("gender", userCriteriaRequest.getGender());
            combinedSpec = combinedSpec.and(genderSpec);
        }
        List<User> userList = this.userRepository.findAll(combinedSpec, pageable).getContent();
        return userList.stream().map(userMapper::toUserFetchRequest).collect(Collectors.toList());
    }
    public User fetchUserByEmail(String email) {
        User user =  this.userRepository.findByEmail(email);
        return user;
    }
    public void handleDeleteUser(Long id) {
        this.userRepository.deleteById(id);
    }
    public UserCreationResponse handleCreateUser(UserCreationRequest userCreationRequest) {
        if(this.userRepository.existsByEmail(userCreationRequest.getEmail())) {
            throw new AppException(ErrorCodeEnum.USER_NOT_EXISTED);
        }
        String hashedPassword = this.passwordEncoder.encode(userCreationRequest.getPassword());
        User user = this.userMapper.toUser(userCreationRequest);
        user.setPassword(hashedPassword);
        User savedUser = this.userRepository.save(user);
        return this.userMapper.toUserResponse(savedUser);
    }
    public UserUpdateResponse handleUpdateUser(UserUpdateRequest userUpdateRequest) {
        User user = this.userRepository.findById(userUpdateRequest.getId()).orElseThrow(() ->
                new AppException(ErrorCodeEnum.USER_NOT_EXISTED));
        this.userMapper.updateUser(user, userUpdateRequest);
        User userAfterUpdate = this.userRepository.save(user);
        return this.userMapper.formUserToUserResponse(userAfterUpdate);
    }
    public UserFetchResponse handleFindUserById(Long id) {
        User user = this.userRepository.findById(id).orElseThrow(() ->
                new AppException(ErrorCodeEnum.USER_NOT_EXISTED));
        return this.userMapper.toUserFetchRequest(user);
    }
    public void updateUserToken(String refresh_token, String email){
        User user = this.userRepository.findByEmail(email);
        if(user == null) {
            user.setRefreshToken(refresh_token);
            this.userRepository.save(user);
        }
    }
    public User getUserByRefreshTokenAndEmail(String token, String email) {
        User user = this.userRepository.findByRefreshTokenAndEmail(token, email);
        return user == null ? new User() : user;
    }
}
