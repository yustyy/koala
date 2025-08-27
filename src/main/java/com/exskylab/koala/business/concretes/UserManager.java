package com.exskylab.koala.business.concretes;


import com.exskylab.koala.business.abstracts.*;

import com.exskylab.koala.core.constants.UserMessages;
import com.exskylab.koala.core.dtos.address.request.CreateAddressRequestDto;
import com.exskylab.koala.core.dtos.user.UpdateUserDto;
import com.exskylab.koala.core.dtos.user.request.UserMeChangePasswordPutRequestDto;
import com.exskylab.koala.core.dtos.user.request.UserMePatchRequestDto;
import com.exskylab.koala.core.dtos.user.request.UsersMeIdentityVerificationRequestDto;
import com.exskylab.koala.core.dtos.user.response.UserUpdateResponseDto;
import com.exskylab.koala.core.exceptions.UserNotFoundException;
import com.exskylab.koala.core.mappers.UserMapper;
import com.exskylab.koala.core.utilities.kps.KimlikPaylasimSistemiService;
import com.exskylab.koala.dataAccess.UserDao;
import com.exskylab.koala.entities.*;
import jakarta.validation.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Service
public class UserManager implements UserService {

    private final UserDao userDao;
    private final Logger logger = LoggerFactory.getLogger(UserManager.class);
    private final EmailVerificationService emailVerificationService;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final SessionService sessionService;
    private final ImageService imageService;
    private final SecurityService securityService;
    private final AddressService addressService;
    private final KimlikPaylasimSistemiService kimlikPaylasimSistemiService;

    public UserManager(UserDao userDao,
                       EmailVerificationService emailVerificationService,
                       UserMapper userMapper,
                       PasswordEncoder passwordEncoder,
                       SessionService sessionService,
                       ImageService imageService,
                       SecurityService securityService,
                       AddressService addressService,
                       KimlikPaylasimSistemiService kimlikPaylasimSistemiService) {
        this.userDao = userDao;
        this.emailVerificationService = emailVerificationService;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.sessionService = sessionService;
        this.imageService = imageService;
        this.securityService = securityService;
        this.addressService = addressService;
        this.kimlikPaylasimSistemiService = kimlikPaylasimSistemiService;
    }

    @Override
    public User getByEmail(String email) {
        return userDao.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("{user.not.found.by.email}"));
    }

    @Override
    public User getByPhoneNumber(String phoneNumber) {
        return null;
    }

    @Override
    public User getById(UUID id) {
        return userDao.findById(id)
                .orElseThrow(() -> new UserNotFoundException("{user.not.found.by.id}"));
    }

    @Override
    public List<User> getAll() {
        return userDao.findAll();
    }

    @Override
    public User save(User user) {
        return userDao.save(user);
    }

    @Override
    public User updateOwnUser(UpdateUserDto updateUserDto) {
        return null;
    }

    @Override
    public User updateUserProfile(UUID userId, UpdateUserDto updateUserDto) {
        return null;
    }

    @Override
    public User delete(UUID id) {
        return null;
    }

    @Override
    public boolean existsByEmail(String email) {
        return userDao.existsByEmail(email);
    }

    @Override
    public boolean existsByPhoneNumber(String phoneNumber) {
        return false;
    }

    @Override
    public boolean existsById(UUID id) {
        return false;
    }

    @Override
    public void changePassword(UUID id, String newUnencodedPassword) {

    }

    @Override
    @Transactional
        public UserUpdateResponseDto patchCurrentUser(UserMePatchRequestDto userMePatchRequestDto) {
        logger.info("Patching current user.");
        User currentUser = securityService.getAuthenticatedUserFromContext();

        List<UserVerification> pendingVerifications = new ArrayList<>();

        logger.info("Patching user with ID: {}", currentUser.getId());

        if (userMePatchRequestDto.getFirstName() != null && userMePatchRequestDto.getFirstName().isEmpty()){
            throw new ValidationException(UserMessages.FIRST_NAME_CANNOT_BE_EMPTY);
        }

        if (userMePatchRequestDto.getLastName() != null && userMePatchRequestDto.getLastName().isEmpty()){
            throw new ValidationException(UserMessages.LAST_NAME_CANNOT_BE_EMPTY);
        }

        if (userMePatchRequestDto.getFirstName() != null) {
            if (currentUser.isIdentityVerified()){
                throw new ValidationException(UserMessages.IDENTITY_VERIFIED_CANNOT_CHANGE_FIRST_NAME);
            }
            logger.info("Updating first name to: {}, from {}", userMePatchRequestDto.getFirstName(), currentUser.getFirstName());
            currentUser.setFirstName(userMePatchRequestDto.getFirstName());
        }

        if (userMePatchRequestDto.getLastName() != null) {
            if (currentUser.isIdentityVerified()){
                throw new ValidationException(UserMessages.IDENTITY_VERIFIED_CANNOT_CHANGE_LAST_NAME);
            }
            logger.info("Updating last name to: {}, from {}", userMePatchRequestDto.getLastName(), currentUser.getLastName());
            currentUser.setLastName(userMePatchRequestDto.getLastName());
        }

        if (userMePatchRequestDto.getBirthdate() != null) {
            if (currentUser.isIdentityVerified()){
                throw new ValidationException(UserMessages.IDENTITY_VERIFIED_CANNOT_CHANGE_BIRTHDATE);
            }
            logger.info("Updating birthdate to: {} from: {}", userMePatchRequestDto.getBirthdate(), currentUser.getBirthDate());
            currentUser.setBirthDate(userMePatchRequestDto.getBirthdate());
        }

        if (userMePatchRequestDto.getGender() != null){
            /*
            if (currentUser.isIdentityVerified()){
                throw new ValidationException(UserMessages.IDENTITY_VERIFIED_CANNOT_CHANGE_GENDER);
            }
             */
            Gender gender;
            try{
                gender = Gender.valueOf(userMePatchRequestDto.getGender().toUpperCase());
            }catch (IllegalArgumentException e){
                throw new ValidationException(UserMessages.GENDER_NOT_FOUND);
            }
            logger.info("Updating gender to: {}, from {}", userMePatchRequestDto.getGender(), currentUser.getGender());
            currentUser.setGender(gender);
            }


        if (userMePatchRequestDto.getPhoneNumber() != null){
            //TODO: IMPLEMENT PHONE NUMBER CHANGE AFTER COMPANY CREATED
        }

        if (userMePatchRequestDto.getEmail() != null) {
            if (!userMePatchRequestDto.getEmail().equals(currentUser.getEmail())) {

                if (existsByEmail(userMePatchRequestDto.getEmail())) {
                    throw new ValidationException(UserMessages.EMAIL_ALREADY_EXISTS);
                }
                logger.info("Updating email to: {}, from {}", userMePatchRequestDto.getEmail(), currentUser.getEmail());
                var emailVerification = emailVerificationService.startEmailVerification(userMePatchRequestDto.getEmail(), currentUser);
                pendingVerifications.add(emailVerification);

                logger.info("Email verification email sent to: {}", userMePatchRequestDto.getEmail());

            } else {
                logger.info("Email is the same as the current user's email. No need to update.");
            }
        }


            if (userMePatchRequestDto.getAbout() != null){
                logger.info("Updating about to: {}, from {}", userMePatchRequestDto.getAbout(), currentUser.getAbout());
                currentUser.setAbout(userMePatchRequestDto.getAbout());
            }

            if (userMePatchRequestDto.getQualifications() != null){
                logger.info("Updating qualifications to: {}, from {}", userMePatchRequestDto.getQualifications(), currentUser.getQualifications());
                currentUser.setQualifications(userMePatchRequestDto.getQualifications());
            }

            if (userMePatchRequestDto.getExperiences() !=null){
                logger.info("Updating experiences to: {}, from {}", userMePatchRequestDto.getExperiences(), currentUser.getExperiences());
                currentUser.setExperiences(userMePatchRequestDto.getExperiences());
            }

            if (userMePatchRequestDto.getInterests() != null){
                logger.info("Updating interests to: {}, from {}", userMePatchRequestDto.getInterests(), currentUser.getInterests());
                currentUser.setInterests(userMePatchRequestDto.getInterests());
            }

            User savedUser = userDao.save(currentUser);

            logger.info("User with ID: {} patched successfully.", savedUser.getId());

            return new UserUpdateResponseDto(userMapper.toUserMeResponseDto(savedUser), pendingVerifications);
    }

    @Override
    public void updateCurrentUserPassword(UserMeChangePasswordPutRequestDto userMeChangePasswordPutRequestDto, UUID currentSessionId) {
        logger.info("Updating current user password.");
        User currentUser = securityService.getAuthenticatedUserFromContext();
        logger.info("Updating password for user with ID: {}", currentUser.getId());

        if (userMeChangePasswordPutRequestDto.getConfirmNewPassword().equals(userMeChangePasswordPutRequestDto.getCurrentPassword())){
            logger.warn("New password is the same as the current password. No need to update.");
            throw new ValidationException(UserMessages.NEW_PASSWORD_CANNOT_BE_SAME_AS_CURRENT_PASSWORD);
        }


        if (!userMeChangePasswordPutRequestDto.getConfirmNewPassword().equals(userMeChangePasswordPutRequestDto.getNewPassword())){
            logger.warn("New password does not match the confirmed new password. No need to update.");
            throw new ValidationException(UserMessages.PASSWORDS_DO_NOT_MATCH);
        }

        var matches = passwordEncoder.matches(userMeChangePasswordPutRequestDto.getCurrentPassword(), currentUser.getPassword());

        if (!matches){
            logger.warn("Current password is incorrect.");
            throw new ValidationException(UserMessages.CURRENT_PASSWORD_INCORRECT);
        }

        String encodedNewPassword = passwordEncoder.encode(userMeChangePasswordPutRequestDto.getNewPassword());

        currentUser.setPassword(encodedNewPassword);

        userDao.save(currentUser);

        sessionService.invalidateActiveSessionsForUserIdExcludingSessionId(currentUser.getId(), currentSessionId);
        logger.info("Password updated successfully for user with ID: {}", currentUser.getId());
    }

    @Override
    @Transactional
    public void updateProfilePicture(MultipartFile image) {
        logger.info("Updating profile picture for current user.");
        var currentUser = securityService.getAuthenticatedUserFromContext();
        logger.info("Uploading new profile picture for user with ID: {}", currentUser.getId());

        Image savedProfilePicture = imageService.uploadImage(image);

        currentUser.setProfilePicture(savedProfilePicture);
        userDao.save(currentUser);
        logger.info("Profile picture updated successfully for user with ID: {}", currentUser.getId());
    }

    @Override
    public void updatePhoneVerification(User user, String newPhoneNumber) {
        logger.info("Updating phone verification for user with ID: {}", user.getId());
        user.setPhoneNumber(newPhoneNumber);
        user.setPhoneVerified(true);
        userDao.save(user);
        logger.info("Phone verification updated successfully for user with ID: {}", user.getId());
    }

    @Override
    public void updateEmailVerification(User user, String newEmail) {
        logger.info("Updating email verification for user with ID: {}", user.getId());
        user.setEmail(newEmail);
        user.setEmailVerified(true);
        userDao.save(user);
        logger.info("Email verification updated successfully for user with ID: {}", user.getId());

    }

    @Override
    public Address addAddressToAuthenticatedUser(CreateAddressRequestDto createAddressRequestDto) {
        logger.info("Adding new address to authenticated user.");
        User currentUser = securityService.getAuthenticatedUserFromContext();
        logger.info("Adding address for user with ID: {}", currentUser.getId());

        var address = addressService.createAddress(createAddressRequestDto);
        return null;
    }

    @Override
    public void verificateIdentityofAuthenticatedUser(UsersMeIdentityVerificationRequestDto usersMeIdentityVerificationRequestDto) {
        logger.info("Verificating identity of authenticated user.");
        User currentUser = securityService.getAuthenticatedUserFromContext();
        logger.info("Verificating identity for user with ID: {}", currentUser.getId());

        if (currentUser.isIdentityVerified()){
            logger.warn("User with ID: {} is already identity verified.", currentUser.getId());
            throw new ValidationException(UserMessages.USER_ALREADY_IDENTITY_VERIFIED);
        }

        boolean isVerified = kimlikPaylasimSistemiService.kimlikDogrula(
                usersMeIdentityVerificationRequestDto.getTcIdentityNumber(),
                usersMeIdentityVerificationRequestDto.getFirstName(),
                usersMeIdentityVerificationRequestDto.getLastName(),
                usersMeIdentityVerificationRequestDto.getBirthDate(),
                usersMeIdentityVerificationRequestDto.getTcDocumentNumber()
        );

        if (!isVerified){
            logger.warn("Identity verification failed for user with ID: {}", currentUser.getId());
            throw new ValidationException(UserMessages.IDENTITY_VERIFICATION_FAILED);
        }

        currentUser.setIdentityVerified(true);
        currentUser.setFirstName(capitalizeFirstLetter(usersMeIdentityVerificationRequestDto.getFirstName()));
        currentUser.setLastName(capitalizeFirstLetter(usersMeIdentityVerificationRequestDto.getLastName()));
        currentUser.setBirthDate(usersMeIdentityVerificationRequestDto.getBirthDate());
        currentUser.setTcIdentityNumber(usersMeIdentityVerificationRequestDto.getTcIdentityNumber());
        userDao.save(currentUser);
        logger.info("Identity verification successful for user with ID: {}", currentUser.getId());

    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }

    private String capitalizeFirstLetter(String input) {
        if (input == null || input.isBlank()) {
            return input;
        }
        input = input.trim();
        return input.substring(0, 1).toUpperCase() + input.substring(1).toLowerCase();
    }

}
