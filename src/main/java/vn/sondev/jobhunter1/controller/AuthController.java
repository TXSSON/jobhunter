package vn.sondev.jobhunter1.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import vn.sondev.jobhunter1.domain.User;
import vn.sondev.jobhunter1.domain.request.LoginRequest;
import vn.sondev.jobhunter1.domain.response.ApiResponse;
import vn.sondev.jobhunter1.domain.response.LoginResponse;
import vn.sondev.jobhunter1.exception.exceptioncustom.AppException;
import vn.sondev.jobhunter1.service.UserService;
import vn.sondev.jobhunter1.util.SecurityUtil;
import vn.sondev.jobhunter1.util.anotation.ApiMessage;
import vn.sondev.jobhunter1.util.enums.ErrorCodeEnum;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/api/v1/auth")
public class AuthController {

    AuthenticationManagerBuilder authenticationManagerBuilder;
    SecurityUtil securityUtil;
    UserService userService;

    @Value(value = "${sondev.jwt.refresh-token-validity-in-seconds}")
    @NonFinal
    long refreshTokenExpiration;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        //Nạp input gồm username/password vào Security
        UsernamePasswordAuthenticationToken authenticationToken
                = new UsernamePasswordAuthenticationToken(loginRequest.getUserName(), loginRequest.getPassWord());
        //xác thực người dùng => cần viết hàm loadUserByUsername
        Authentication authentication =
                authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        //tạo token

        LoginResponse.UserLogin userLogin = new LoginResponse.UserLogin();

        User currentUser = this.userService.fetchUserByEmail(loginRequest.getUserName());

        LoginResponse loginResponse = new LoginResponse();
        if (currentUser != null) {
            userLogin.setName(currentUser.getName());
            userLogin.setEmail(currentUser.getEmail());
            userLogin.setId(currentUser.getId());
            loginResponse.setUserLogin(userLogin);
        }
        String access_token = this.securityUtil.createAccessToken(currentUser.getEmail(), loginResponse.getUserLogin());
        String refresh_token = this.securityUtil.createRefreshToken(loginRequest.getUserName(), loginResponse);
        // update user
        this.userService.updateUserToken(refresh_token, loginRequest.getUserName());
        loginResponse.setAccess_token(access_token);
        // set cookies
        ResponseCookie resCookies = ResponseCookie
                .from("refresh_token", refresh_token)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(refreshTokenExpiration)
                .build();
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, resCookies.toString())
                .body(loginResponse);
    }

    @GetMapping("/account")
    @ApiMessage("fetch account")
    public ResponseEntity<LoginResponse.UserLogin> getAccount() {
        String email = SecurityUtil.getCurrentUserLogin().isPresent()
                ? SecurityUtil.getCurrentUserLogin().get() : "";

        User currentUserDB = this.userService.fetchUserByEmail(email);
        LoginResponse.UserLogin userLogin = new LoginResponse.UserLogin();
        if (currentUserDB != null) {
            userLogin.setId(currentUserDB.getId());
            userLogin.setEmail(currentUserDB.getEmail());
            userLogin.setName(currentUserDB.getName());
        }
        return ResponseEntity.ok().body(userLogin);
    }

    @GetMapping("/refresh")
    @ApiMessage("Get User by refresh token")
    public ResponseEntity<LoginResponse> getRefreshToken(
            @CookieValue(name = "refresh_token", defaultValue = "abc") String refresh_token) throws AppException {
        if (refresh_token.equals("abc")) {
            throw new AppException(ErrorCodeEnum.USER_NOT_EXISTED, "Bạn không có refresh token ở cookie");
        }
        // check valid
        Jwt decodedToken = this.securityUtil.checkValidRefreshToken(refresh_token);
        String email = decodedToken.getSubject();

        // check user by token + email
        User currentUser = this.userService.getUserByRefreshTokenAndEmail(refresh_token, email);
        if (currentUser == null) {
            throw new AppException(ErrorCodeEnum.USER_NOT_EXISTED, "Refresh Token không hợp lệ");
        }

        // issue new token/set refresh token as cookies
        LoginResponse res = new LoginResponse();
        User currentUserDB = this.userService.fetchUserByEmail(email);
        if (currentUserDB != null) {
            LoginResponse.UserLogin userLogin = new LoginResponse.UserLogin(
                    currentUserDB.getId(),
                    currentUserDB.getEmail(),
                    currentUserDB.getName());
            res.setUserLogin(userLogin);
        }

        // create access token
        String access_token = this.securityUtil.createAccessToken(email, res.getUserLogin());
        res.setAccess_token(access_token);

        // create refresh token
        String new_refresh_token = this.securityUtil.createRefreshToken(email, res);

        // update user
        this.userService.updateUserToken(new_refresh_token, email);

        // set cookies
        ResponseCookie resCookies = ResponseCookie
                .from("refresh_token", new_refresh_token)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(refreshTokenExpiration)
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, resCookies.toString())
                .body(res);
    }

    @PostMapping("/logout")
    @ApiMessage("Logout User")
    public ResponseEntity<Void> logout() throws AppException {
        String email = SecurityUtil.getCurrentUserLogin().isPresent() ? SecurityUtil.getCurrentUserLogin().get() : "";

        if (email.equals("")) {
            throw new AppException(ErrorCodeEnum.USER_NOT_EXISTED, "Access Token không hợp lệ");
        }

        // update refresh token = null
        this.userService.updateUserToken(null, email);

        // remove refresh token cookie
        ResponseCookie deleteSpringCookie = ResponseCookie
                .from("refresh_token", null)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(0)
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, deleteSpringCookie.toString())
                .body(null);
    }

}
