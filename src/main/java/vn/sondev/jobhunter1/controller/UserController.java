package vn.sondev.jobhunter1.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import vn.sondev.jobhunter1.domain.request.UserCreationRequest;
import vn.sondev.jobhunter1.domain.request.UserCriteriaRequest;
import vn.sondev.jobhunter1.domain.request.UserUpdateRequest;
import vn.sondev.jobhunter1.domain.response.UserCreationResponse;
import vn.sondev.jobhunter1.domain.response.UserFetchResponse;
import vn.sondev.jobhunter1.domain.response.UserUpdateResponse;
import vn.sondev.jobhunter1.service.UserService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/api/v1/users")
public class UserController {
    UserService userService;

    @GetMapping
    public List<UserFetchResponse> getAllUser(@ModelAttribute UserCriteriaRequest userCriteriaRequest, Pageable pageable) {
        return this.userService.fetchAllUserWithConditions(userCriteriaRequest, pageable);
    }
    @PostMapping
    public UserCreationResponse createUser(@RequestBody UserCreationRequest userCreationRequest) {
        return this.userService.handleCreateUser(userCreationRequest);
    }
    @PutMapping
    public UserUpdateResponse updateUser(@RequestBody UserUpdateRequest userUpdateRequest) {
        return this.userService.handleUpdateUser(userUpdateRequest);
    }
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable(name = "id") Long id) {
        this.userService.handleDeleteUser(id);
    }
    @GetMapping("/{id}")
    public UserFetchResponse getUser(@PathVariable(name = "id") Long id) {
    return this.userService.handleFindUserById(id);
    }
}
