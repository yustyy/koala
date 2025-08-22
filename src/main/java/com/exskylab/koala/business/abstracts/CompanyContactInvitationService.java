package com.exskylab.koala.business.abstracts;

import com.exskylab.koala.entities.Company;
import com.exskylab.koala.entities.CompanyContactInvitation;
import com.exskylab.koala.entities.CompanyContactRole;
import com.exskylab.koala.entities.User;

import java.util.List;

public interface CompanyContactInvitationService {
    CompanyContactInvitation inviteUserToCompany(User invitedBy, User userToInvite, Company company, CompanyContactRole role);

    List<CompanyContactInvitation> getInvitationsByCompany(Company company);
}
