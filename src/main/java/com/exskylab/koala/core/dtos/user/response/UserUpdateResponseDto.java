package com.exskylab.koala.core.dtos.user.response;

import com.exskylab.koala.core.dtos.userVerification.response.VerificationInfoDto;
import com.exskylab.koala.entities.UserVerification;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class UserUpdateResponseDto {

    private UserMeResponseDto user;

    private List<VerificationInfoDto> pendingVerifications;


    public UserUpdateResponseDto(UserMeResponseDto userMeResponseDto, List<UserVerification> verifications) {

        this.user = userMeResponseDto;

        this.pendingVerifications = verifications.stream()
                .map(VerificationInfoDto::new)
                .toList();

    }
}
