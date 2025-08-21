package com.exskylab.koala.core.dtos.companyContact.request;


import com.exskylab.koala.core.constants.CompanyContactMessages;
import com.exskylab.koala.entities.CompanyContactRole;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InviteContactToCompanyDto {

    @NotBlank(message = CompanyContactMessages.TC_IDENTITY_NUMBER_NOT_BLANK)
    private String tcIdentityNumber;

    @NotBlank(message = CompanyContactMessages.FIRST_NAME_NOT_BLANK)
    private String email;

    @NotBlank(message = CompanyContactMessages.COMPANY_ID_NOT_BLANK)
    @UUID(message = CompanyContactMessages.INVALID_UUID)
    private java.util.UUID companyId;

    @NotBlank(message = CompanyContactMessages.ROLE_NOT_BLANK)
    private CompanyContactRole role;

}
