package com.exskylab.koala.business.concretes;

import com.exskylab.koala.business.abstracts.*;
import com.exskylab.koala.core.constants.CompanyMessages;
import com.exskylab.koala.core.dtos.company.request.CreateCompanyRequestDto;
import com.exskylab.koala.core.dtos.companyContact.request.InviteContactToCompanyDto;
import com.exskylab.koala.core.dtos.notification.request.SendEmailDto;
import com.exskylab.koala.core.exceptions.CompanyNotFoundException;
import com.exskylab.koala.core.exceptions.UserNotAssosiatedWithCompanyException;
import com.exskylab.koala.dataAccess.CompanyDao;
import com.exskylab.koala.entities.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class CompanyManager implements CompanyService {

    private final CompanyDao companyDao;
    private final UserService userService;
    private final NotificationService notificationService;
    private final ImageService imageService;
    private final CompanyContactService companyContactService;
    private final CompanyContactInvitationService companyContactInvitationService;

    private final static Logger logger = LoggerFactory.getLogger(CompanyManager.class);

    public CompanyManager(CompanyDao companyDao, UserService userService,
                          NotificationService notificationService, ImageService imageService,
                          CompanyContactService companyContactService,
                          CompanyContactInvitationService companyContactInvitationService) {
        this.companyDao = companyDao;
        this.userService = userService;
        this.notificationService = notificationService;
        this.imageService = imageService;
        this.companyContactService = companyContactService;
        this.companyContactInvitationService = companyContactInvitationService;
    }

    @Override
    @Transactional
    public Company addCompany(CreateCompanyRequestDto createCompanyRequestDto, MultipartFile logo) {
        CompanyType companyType;
        try {
            companyType = CompanyType.valueOf(createCompanyRequestDto.getType());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(CompanyMessages.INVALID_COMPANY_TYPE);
        }

        logger.info("Adding new company with name: {}", createCompanyRequestDto.getName());
        var user = userService.getAuthenticatedUser();
        logger.info("User with id: {} is adding a new company.", user.getId());

        var company = new Company();
        company.setName(createCompanyRequestDto.getName());
        company.setEmail(createCompanyRequestDto.getEmail());
        company.setPhoneNumber(createCompanyRequestDto.getPhoneNumber());
        company.setWebsite(createCompanyRequestDto.getWebsite());
        company.setDescription(createCompanyRequestDto.getDescription());
        company.setTaxNumber(createCompanyRequestDto.getTaxNumber());
        company.setType(companyType);
        company.setApproved(false);

        if (logo != null && !logo.isEmpty()) {
            var image = imageService.uploadImage(logo);
            company.setLogo(image);
            logger.info("Logo uploaded for company with id: {}", company.getId());
        }
        logger.info("New company with id: {} added successfully.", company.getId());


        var companyContact = new CompanyContact();
        companyContact.setRole(CompanyContactRole.ADMIN);
        companyContact.setUser(user);

        company.addContact(companyContact);

        var savedCompany = companyDao.save(company);
        logger.info("Company contact created for user with id: {} and company with id: {}", user.getId(), company.getId());


        sendCompanyCreatedMail(savedCompany, companyContact);

        logger.info("Notification sent to user with id: {} for company with id: {}", user.getId(), company.getId());


        return savedCompany;

    }

    @Override
    @Transactional
    public void inviteContactToCompany(UUID companyId, InviteContactToCompanyDto contactToCompanyDto) {
        logger.info("Inviting contact to company with id: {}", companyId);


        CompanyContactRole role;
        try {
            role = CompanyContactRole.valueOf(contactToCompanyDto.getRole());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(CompanyMessages.INVALID_COMPANY_CONTACT_ROLE);
        }


        var currentUser = userService.getAuthenticatedUser();


        var company = companyDao.findById(companyId).orElseThrow(() -> {
            logger.error("Company with id: {} not found.", companyId);
            return new CompanyNotFoundException(CompanyMessages.COMPANY_NOT_FOUND);
        });

        boolean isAdmin = company.getContacts().stream()
                .anyMatch(contact ->
                        contact.getUser().getId().equals(currentUser.getId()) &&
                        contact.getRole().equals(CompanyContactRole.ADMIN));

        if (!isAdmin){
            logger.warn("User with id: {} is not an admin of company with id: {}, terminating request!", currentUser.getId(), companyId);
            throw new UserNotAssosiatedWithCompanyException(CompanyMessages.USER_NOT_ASSOCIATED_WITH_COMPANY);
        }

        var userToInvite = userService.getByEmail(contactToCompanyDto.getEmail());

        boolean isContactAlreadyExists = company.getContacts().stream()
                .anyMatch(contact -> contact.getUser().getId().equals(userToInvite.getId()));

        if (isContactAlreadyExists){
            logger.warn("User with id: {} is already a contact of company with id: {}, terminating request!", userToInvite.getId(), companyId);
            throw new IllegalArgumentException(CompanyMessages.USER_ALREADY_A_CONTACT);
        }

        if (userToInvite.getTcIdentityNumber() == null) {
            throw new IllegalArgumentException(CompanyMessages.USER_MUST_VERIFY_IDENTITY);
        }

        if (!userToInvite.getTcIdentityNumber().equals(contactToCompanyDto.getTcIdentityNumber())){
            throw new IllegalArgumentException(CompanyMessages.USER_TC_IDENTITY_DOES_NOT_MATCH);
        }

        companyContactInvitationService.inviteUserToCompany(currentUser, userToInvite, company, role);

        logger.info("Contact with user id: {} invited to company with id: {} successfully.", userToInvite.getId(), companyId);

    }

    @Override
    public List<CompanyContactInvitation> getCompanyContactInvitations(UUID companyId) {
        logger.info("Getting company contact invitations for company with id: {}", companyId);
        var currentUser = userService.getAuthenticatedUser();

        var company = companyDao.findById(companyId).orElseThrow(() -> {
            logger.error("Company with id: {} not found.", companyId);
            return new CompanyNotFoundException(CompanyMessages.COMPANY_NOT_FOUND);
        });

        boolean isAdmin = company.getContacts().stream()
                .anyMatch(contact ->
                        contact.getUser().getId().equals(currentUser.getId())
                                && contact.getRole().equals(CompanyContactRole.ADMIN)
                );

        if (!isAdmin){
            logger.warn("User with id: {} is not a contact of company with id: {}, terminating request!", currentUser.getId(), companyId);
            throw new UserNotAssosiatedWithCompanyException(CompanyMessages.USER_NOT_ASSOCIATED_WITH_COMPANY);
        }

        var invitations = companyContactInvitationService.getInvitationsByCompany(company);

        logger.info("Company contact invitations for company with id: {} retrieved successfully.", companyId);

        return invitations;
    }

    private void sendCompanyCreatedMail(Company savedCompany, CompanyContact contact ) {
        logger.info("Sending company created email for company with id: {}", savedCompany.getId());

        var sendEmailDto = new SendEmailDto();
        sendEmailDto.setDestinationEmail(savedCompany.getEmail());
        sendEmailDto.setSubject("KOALA ŞİRKET OLUŞTURMA TALEBİNİZ ALINMIŞTIR");
        sendEmailDto.setTemplateName("company-created-to-company");
        sendEmailDto.setTemplateParameters(Map.of(
                "companyName", savedCompany.getName(),
                "companyId", savedCompany.getId().toString(),
                "companyEmail", savedCompany.getEmail()
        ));
        sendEmailDto.setCategory(NotificationCategory.ACCOUNT_SECURITY);
        notificationService.sendEmail(sendEmailDto, DispatchPriority.NORMAL, true);
        logger.info("Company created email sent to: {}", sendEmailDto.getDestinationEmail());

        var sendEmailDtoToContact = new SendEmailDto();
        sendEmailDtoToContact.setDestinationEmail(contact.getUser().getEmail());
        sendEmailDtoToContact.setSubject("KOALA ŞİRKET OLUŞTURMA TALEBİNİZ ALINMIŞTIR");
        sendEmailDtoToContact.setTemplateName("company-created-to-contact");
        sendEmailDtoToContact.setTemplateParameters(Map.of(
                "companyName", savedCompany.getName(),
                "companyId", savedCompany.getId().toString(),
                "contactFirstName", contact.getUser().getFirstName(),
                "contactLastName", contact.getUser().getLastName(),
                "contactId", contact.getId().toString(),
                "contactRole", contact.getRole().name(),
                "contactEmail", contact.getUser().getEmail()
        ));
        sendEmailDtoToContact.setCategory(NotificationCategory.ACCOUNT_SECURITY);
        notificationService.sendEmail(sendEmailDtoToContact, DispatchPriority.NORMAL, true);
        logger.info("Company created email sent to contact: {}", sendEmailDtoToContact.getDestinationEmail());

        logger.info("Company created emails sent successfully for company with id: {}", savedCompany.getId());
    }
}
