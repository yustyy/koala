package com.exskylab.koala.core.mappers;

import com.exskylab.koala.core.dtos.user.response.SafeUserDto;
import com.exskylab.koala.core.dtos.user.response.UserMeResponseDto;
import com.exskylab.koala.core.properties.R2Properties;
import com.exskylab.koala.entities.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    private final R2Properties r2Properties;
    private final AddressMapper addressMapper;

    public UserMapper(R2Properties r2Properties, AddressMapper addressMapper) {
        this.r2Properties = r2Properties;
        this.addressMapper = addressMapper;
    }

    public SafeUserDto toSafeUserDto(User user) {
        if (user == null) {
            return null;
        }
        var safeUserDto = new SafeUserDto();
        safeUserDto.setId(user.getId());
        safeUserDto.setFirstName(user.getFirstName());
        safeUserDto.setLastName(user.getLastName());
        safeUserDto.setUsername(user.getUsername());
        safeUserDto.setProfilePictureUrl(user.getProfilePicture() != null ? r2Properties.getPublicUrl()+"/"+user.getProfilePicture().getFileUrl() : null);
        return safeUserDto;
    }

    public UserMeResponseDto toUserMeResponseDto(User user) {
        if (user == null) {
            return null;
        }

        UserMeResponseDto userDto = new UserMeResponseDto();

        userDto.setId(user.getId());
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        userDto.setEmail(user.getEmail());
        userDto.setProfilePictureUrl(user.getProfilePicture() != null ? r2Properties.getPublicUrl()+"/"+user.getProfilePicture().getFileUrl() : null);
        userDto.setPhoneNumber(user.getPhoneNumber() != null ? user.getPhoneNumber() : null);
        userDto.setBirthDate(user.getBirthDate() != null ? user.getBirthDate() : null);
        userDto.setGender(user.getGender() != null ? user.getGender() : null);
        userDto.setTcIdentityNumber(user.getTcIdentityNumber() != null ? user.getTcIdentityNumber() : null);
        userDto.setAbout(user.getAbout() != null ? user.getAbout() : null);
        userDto.setQualifications(user.getQualifications() != null ? user.getQualifications() : null);
        userDto.setInterests(user.getInterests() != null ? user.getInterests() : null);
        userDto.setAddress(user.getAddress() != null ? addressMapper.toAddressDto(user.getAddress()) : null);
        return userDto;

    }
}
