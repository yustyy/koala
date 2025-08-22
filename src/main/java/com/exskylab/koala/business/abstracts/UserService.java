package com.exskylab.koala.business.abstracts;


import com.exskylab.koala.core.dtos.user.UpdateUserDto;
import com.exskylab.koala.core.dtos.user.request.UserMeChangePasswordPutRequestDto;
import com.exskylab.koala.core.dtos.user.request.UserMePatchRequestDto;
import com.exskylab.koala.core.dtos.user.response.UserUpdateResponseDto;
import com.exskylab.koala.entities.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface UserService extends UserDetailsService {

    User getByEmail(String email);
    User getByPhoneNumber(String phoneNumber);
    User getById(UUID id);
    List<User> getAll();
    User getAuthenticatedUser();

    User save(User user);
    User updateOwnUser(UpdateUserDto updateUserDto);
    User updateUserProfile(UUID userId, UpdateUserDto updateUserDto);


    User delete(UUID id);

    boolean existsByEmail(String email);
    boolean existsByPhoneNumber(String phoneNumber);
    boolean existsById(UUID id);

    void changePassword(UUID id, String newUnencodedPassword);



    User getSystemUser();

    UserUpdateResponseDto patchCurrentUser(UserMePatchRequestDto userMePatchRequestDto);

    void updateCurrentUserPassword(UserMeChangePasswordPutRequestDto userMeChangePasswordPutRequestDto, UUID currentSessionId);

    void updateProfilePicture(MultipartFile image);

    void updatePhoneVerification(User user, String newPhoneNumber);

    void updateEmailVerification(User user, String newEmail);
}
