package com.exskylab.koala.business.concretes;

import com.exskylab.koala.business.abstracts.CompanyContactInvitationService;
import com.exskylab.koala.business.abstracts.NotificationService;
import com.exskylab.koala.core.dtos.notification.request.SendEmailDto;
import com.exskylab.koala.dataAccess.CompanyContactInvitationDao;
import com.exskylab.koala.entities.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class CompanyContactInvitationManager implements CompanyContactInvitationService {


    private final CompanyContactInvitationDao companyContactInvitationDao;
    private final NotificationService notificationService;

    private final Logger logger = LoggerFactory.getLogger(CompanyContactInvitationManager.class);

    public CompanyContactInvitationManager(CompanyContactInvitationDao companyContactInvitationDao,
                                           NotificationService notificationService) {
        this.companyContactInvitationDao = companyContactInvitationDao;
        this.notificationService = notificationService;
    }


    @Override
    public CompanyContactInvitation inviteUserToCompany(User invitedBy, User userToInvite, Company company, CompanyContactRole role) {
        logger.info("Inviting user with id: {} to company with id: {}", userToInvite.getId(), company.getId());
        CompanyContactInvitation invitation = new CompanyContactInvitation();
        invitation.setCompany(company);
        invitation.setInvitedBy(invitedBy);
        invitation.setInvitedUser(userToInvite);
        invitation.setRole(role);
        invitation.setStatus(CompanyContactInvitationStatus.PENDING);
        invitation.setInvitedAt(LocalDateTime.now());
        invitation.setAnsweredAt(null);
        CompanyContactInvitation savedInvitation = companyContactInvitationDao.save(invitation);

        logger.info("Saved invitation with id: {}", savedInvitation.getId());

        SendEmailDto sendEmailDto = new SendEmailDto();
        sendEmailDto.setRecipientId(userToInvite.getId());
        sendEmailDto.setCategory(NotificationCategory.ACCOUNT_SECURITY);
        sendEmailDto.setDestinationEmail(userToInvite.getEmail());

        sendEmailDto.setTemplateName("company-invitation-template");
        sendEmailDto.setTemplateParameters(Map.of(
                "firstName", userToInvite.getFirstName(),
                "lastName", userToInvite.getLastName(),
                "companyName", company.getName(),
                "invitedByFirstName", invitedBy.getFirstName(),
                "invitedByLastName", invitedBy.getLastName(),
                "role", role.name()
        ));
        logger.info("Sending notification to user with id: {} about the invitation.", userToInvite.getId());

        notificationService.sendEmail(sendEmailDto, DispatchPriority.CRITICAL, true);

        return savedInvitation;
    }

    @Override
    public List<CompanyContactInvitation> getInvitationsByCompany(Company company) {
        return companyContactInvitationDao.findByCompany(company);
    }
}
