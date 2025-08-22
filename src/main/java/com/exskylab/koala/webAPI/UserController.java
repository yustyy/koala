package com.exskylab.koala.webAPI;

import com.exskylab.koala.business.abstracts.SecurityService;
import com.exskylab.koala.business.abstracts.UserService;
import com.exskylab.koala.core.constants.UserMessages;
import com.exskylab.koala.core.dtos.user.request.UserMeChangePasswordPutRequestDto;
import com.exskylab.koala.core.dtos.user.request.UserMePatchRequestDto;
import com.exskylab.koala.core.dtos.user.response.UserMeResponseDto;
import com.exskylab.koala.core.dtos.user.response.UserUpdateResponseDto;
import com.exskylab.koala.core.mappers.UserMapper;
import com.exskylab.koala.core.security.JwtService;
import com.exskylab.koala.core.utilities.results.SuccessDataResult;
import com.exskylab.koala.core.utilities.results.SuccessResult;
import com.exskylab.koala.entities.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Validated
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;
    private final JwtService jwtService;
    private final SecurityService securityService;

    public UserController(UserService userService, UserMapper userMapper, JwtService jwtService, SecurityService securityService) {
        this.userService = userService;
        this.userMapper= userMapper;
        this.jwtService = jwtService;
        this.securityService = securityService;
    }

   @GetMapping("/me")
    public ResponseEntity<SuccessDataResult<UserMeResponseDto>> getCurrentUser(){

        User currentUser = securityService.getAuthenticatedUser();

        var userDto =  userMapper.toUserMeResponseDto(currentUser);

        return ResponseEntity.status(HttpStatus.OK).body(
                new SuccessDataResult<UserMeResponseDto>(
                        userDto,
                        UserMessages.USER_RETRIEVED_SUCCESSFULLY,
                        HttpStatus.OK

                ));
    }


    @PostMapping("/me")
    public ResponseEntity<SuccessDataResult<UserUpdateResponseDto>> patchCurrentUser(@RequestBody @Valid UserMePatchRequestDto userMePatchRequestDto){

        UserUpdateResponseDto patchedUser = userService.patchCurrentUser(userMePatchRequestDto);

        return ResponseEntity.status(HttpStatus.OK).body(
                new SuccessDataResult<UserUpdateResponseDto>(
                        patchedUser,
                        UserMessages.USER_UPDATED_SUCCESSFULLY,
                        HttpStatus.OK));
    }


    @PostMapping("/me/password")
    public ResponseEntity<SuccessResult> updateCurrentUserPassword(@RequestBody @Valid UserMeChangePasswordPutRequestDto userMeChangePasswordPutRequestDto,
                                                                   HttpServletRequest request){
        String authHeader = request.getHeader("Authorization");
        String token = authHeader.substring(7);
        UUID currentSessionId = jwtService.extractSessionId(token);

        userService.updateCurrentUserPassword(userMeChangePasswordPutRequestDto, currentSessionId);

        return ResponseEntity.status(HttpStatus.OK).body(
                new SuccessResult(
                        UserMessages.USER_PASSWORD_UPDATED_SUCCESSFULLY,
                        HttpStatus.OK));
    }

    @PostMapping("/me/profile-picture")
    public ResponseEntity<SuccessResult> uploadProfilePicture(@RequestParam("image") MultipartFile image){

        userService.updateProfilePicture(image);

        return ResponseEntity.status(HttpStatus.OK).body(
                new SuccessResult(
                        UserMessages.USER_PROFILE_PICTURE_UPLOADED_SUCCESSFULLY,
                        HttpStatus.OK));
    }





}
