package com.exskylab.koala.business.concretes;

import com.exskylab.koala.business.abstracts.CompanyContactInvitationService;
import com.exskylab.koala.business.abstracts.NotificationService;
import com.exskylab.koala.business.abstracts.UserService;
import com.exskylab.koala.core.constants.CompanyContactInvitationMessages;
import com.exskylab.koala.core.dtos.notification.request.SendEmailDto;
import com.exskylab.koala.core.exceptions.CompanyContactInvitationNotFoundException;
import com.exskylab.koala.core.exceptions.InvitationAlreadyAnsweredException;
import com.exskylab.koala.core.exceptions.UserNotAssosiatedWithInvitationException;
import com.exskylab.koala.dataAccess.CompanyContactInvitationDao;
import com.exskylab.koala.entities.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class CompanyContactInvitationManager implements CompanyContactInvitationService {


    private final CompanyContactInvitationDao companyContactInvitationDao;
    private final NotificationService notificationService;

    private final Logger logger = LoggerFactory.getLogger(CompanyContactInvitationManager.class);
    private final UserService userService;

    public CompanyContactInvitationManager(CompanyContactInvitationDao companyContactInvitationDao,
                                           NotificationService notificationService, UserService userService) {
        this.companyContactInvitationDao = companyContactInvitationDao;
        this.notificationService = notificationService;
        this.userService = userService;
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

    @Override
    public CompanyContactInvitation answerToInvitation(UUID invitationId, boolean accepted) {
        logger.info("Answering invitation with id: {} to accepted: {}", invitationId, accepted);
        CompanyContactInvitation invitation = companyContactInvitationDao.findById(invitationId)
                .orElseThrow(() -> new CompanyContactInvitationNotFoundException(CompanyContactInvitationMessages.COMPANY_CONTACT_INVITATION_NOT_FOUND));

        var currentUser = userService.getAuthenticatedUser();

        if (!invitation.getInvitedUser().getId().equals(currentUser.getId())){
            throw new UserNotAssosiatedWithInvitationException(CompanyContactInvitationMessages.USER_NOT_ASSOCIATED_WITH_INVITATION);
        }

        if (invitation.getStatus() != CompanyContactInvitationStatus.PENDING) {
            logger.warn("Invitation with id: {} has already been answered.", invitationId);
            throw new InvitationAlreadyAnsweredException(CompanyContactInvitationMessages.INVITATION_ALREADY_ANSWERED);
        }

        invitation.setStatus(accepted ? CompanyContactInvitationStatus.ACCEPTED : CompanyContactInvitationStatus.REJECTED);

        invitation.setAnsweredAt(LocalDateTime.now());

        companyContactInvitationDao.save(invitation);

        logger.info("Invitation with id: {} has been answered and saved.", invitationId);

        SendEmailDto invitedUsersEmailNotification = new SendEmailDto();
        invitedUsersEmailNotification.setRecipientId(invitation.getInvitedUser().getId());
        invitedUsersEmailNotification.setCategory(NotificationCategory.ACCOUNT_SECURITY);
        invitedUsersEmailNotification.setDestinationEmail(invitation.getInvitedUser().getEmail());
        invitedUsersEmailNotification.setTemplateName(accepted ? "company-invitation-accepted-template" : "company-invitation-rejected-template");
        invitedUsersEmailNotification.setTemplateParameters(Map.of(
                "firstName", invitation.getInvitedUser().getFirstName(),
                "lastName", invitation.getInvitedUser().getLastName(),
                "companyName", invitation.getCompany().getName(),
                "role", invitation.getRole().name()
        ));

        notificationService.sendEmail(invitedUsersEmailNotification, DispatchPriority.NORMAL, true);

        SendEmailDto invitedByEmailNotification = new SendEmailDto();
        invitedByEmailNotification.setRecipientId(invitation.getInvitedBy().getId());
        invitedByEmailNotification.setCategory(NotificationCategory.ACCOUNT_SECURITY);
        invitedByEmailNotification.setDestinationEmail(invitation.getInvitedBy().getEmail());
        invitedByEmailNotification.setTemplateName(accepted ? "company-invitation-accepted-by-inviter-template" : "company-invitation-rejected-by-inviter-template");
        invitedByEmailNotification.setTemplateParameters(Map.of(
                "firstName", invitation.getInvitedBy().getFirstName(),
                "lastName", invitation.getInvitedBy().getLastName(),
                "companyName", invitation.getCompany().getName(),
                "role", invitation.getRole().name(),
                "invitedUserFirstName", invitation.getInvitedUser().getFirstName(),
                "invitedUserLastName", invitation.getInvitedUser().getLastName()
        ));

        notificationService.sendEmail(invitedByEmailNotification, DispatchPriority.NORMAL, true);
        logger.info("Notifications about the answered invitation have been sent.");
        return invitation;
    }

    @Override
    public CompanyContactInvitation getInvitationById(UUID invitationId) {
        logger.info("Getting invitation with id: {}", invitationId);
        return companyContactInvitationDao.findById(invitationId)
                .orElseThrow(() -> new CompanyContactInvitationNotFoundException(CompanyContactInvitationMessages.COMPANY_CONTACT_INVITATION_NOT_FOUND));
    }
}
