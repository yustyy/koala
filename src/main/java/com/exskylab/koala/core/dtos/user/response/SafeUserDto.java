package com.exskylab.koala.core.dtos.user.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SafeUserDto {

    private UUID id;

    private String firstName;

    private String lastName;

    private String username;

    private String profilePictureUrl;

}
