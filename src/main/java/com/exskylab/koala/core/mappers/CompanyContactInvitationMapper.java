package com.exskylab.koala.core.mappers;

import com.exskylab.koala.core.dtos.companyContactInvitation.response.CompanyContactInvitationDto;
import com.exskylab.koala.core.dtos.companyContactInvitation.response.GetCompanyContactInvitationDto;
import com.exskylab.koala.entities.CompanyContactInvitation;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CompanyContactInvitationMapper {

    private final UserMapper userMapper;
    private final CompanyMapper companyMapper;

    public CompanyContactInvitationMapper(UserMapper userMapper, CompanyMapper companyMapper) {
        this.userMapper = userMapper;
        this.companyMapper = companyMapper;
    }

    public CompanyContactInvitationDto toCompanyContactInvitationDto(CompanyContactInvitation companyContactInvitation) {
        CompanyContactInvitationDto dto = new CompanyContactInvitationDto();
        dto.setId(companyContactInvitation.getId());
        dto.setInvitedBy(userMapper.toSafeUserDto(companyContactInvitation.getInvitedBy()));
        dto.setInvitedUser(userMapper.toSafeUserDto(companyContactInvitation.getInvitedUser()));
        dto.setRole(companyContactInvitation.getRole());
        dto.setStatus(companyContactInvitation.getStatus());
        dto.setInvitedAt(companyContactInvitation.getInvitedAt());
        dto.setAnsweredAt(companyContactInvitation.getAnsweredAt());

        return dto;
    }


    public List<CompanyContactInvitationDto> toCompanyContactInvitationDtoList(List<CompanyContactInvitation> companyContactInvitations) {
        return companyContactInvitations.stream().map(this::toCompanyContactInvitationDto).toList();
    }

    public GetCompanyContactInvitationDto toGetCompanyContactInvitationDto(CompanyContactInvitation companyContactInvitation) {
        GetCompanyContactInvitationDto dto = new GetCompanyContactInvitationDto();
        dto.setId(companyContactInvitation.getId());
        dto.setCompany(companyMapper.toOnlyCompanyDto(companyContactInvitation.getCompany()));
        dto.setInvitedBy(userMapper.toSafeUserDto(companyContactInvitation.getInvitedBy()));
        dto.setInvitedUser(userMapper.toSafeUserDto(companyContactInvitation.getInvitedUser()));
        dto.setRole(companyContactInvitation.getRole());
        dto.setStatus(companyContactInvitation.getStatus());
        dto.setInvitedAt(companyContactInvitation.getInvitedAt());
        dto.setAnsweredAt(companyContactInvitation.getAnsweredAt());
        return dto;

    }
}
