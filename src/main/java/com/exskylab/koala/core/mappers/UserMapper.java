package com.exskylab.koala.core.mappers;

import com.exskylab.koala.core.dtos.user.response.SafeUserDto;
import com.exskylab.koala.core.dtos.user.response.UserMeResponseDto;
import com.exskylab.koala.core.properties.R2Properties;
import com.exskylab.koala.entities.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    private final R2Properties r2Properties;

    public UserMapper(R2Properties r2Properties) {
        this.r2Properties = r2Properties;
    }

    public SafeUserDto toSafeUserDto(User user) {
        var safeUserDto = new SafeUserDto();
        safeUserDto.setId(user.getId());
        safeUserDto.setFirstName(user.getFirstName());
        safeUserDto.setLastName(user.getLastName());
        safeUserDto.setUsername(user.getUsername());
        safeUserDto.setProfilePictureUrl(user.getProfilePicture() != null ? r2Properties.getPublicUrl()+"/"+user.getProfilePicture().getFileUrl() : null);
        return safeUserDto;
    }

    public UserMeResponseDto toUserMeResponseDto(User user) {

        UserMeResponseDto userDto = new UserMeResponseDto();

        userDto.setId(user.getId());
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        userDto.setEmail(user.getEmail());
        userDto.setProfilePictureUrl(user.getProfilePicture() != null ? r2Properties.getPublicUrl()+"/"+user.getProfilePicture().getFileUrl() : null);
        userDto.setPhoneNumber(user.getPhoneNumber());
        userDto.setBirthDate(user.getBirthDate());
        userDto.setGender(user.getGender().toString());
        userDto.setTcIdentityNumber(user.getTcIdentityNumber());
        userDto.setAbout(user.getAbout());
        userDto.setQualifications(user.getQualifications());
        userDto.setInterests(user.getInterests());
        return userDto;

    }
}
