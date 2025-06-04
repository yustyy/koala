package com.exskylab.koala.business.concretes;

import com.exskylab.koala.business.abstracts.UserService;
import com.exskylab.koala.business.abstracts.VerificationService;
import com.exskylab.koala.core.constants.messages.UserMessages;
import com.exskylab.koala.core.constants.messages.VerificationMessages;
import com.exskylab.koala.core.utilities.exceptions.*;
import com.exskylab.koala.dataAccess.VerificationDao;
import com.exskylab.koala.entities.User;
import com.exskylab.koala.entities.Verification;
import com.exskylab.koala.entities.VerificationStatus;
import com.exskylab.koala.entities.VerificationType;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class VerificationManager implements VerificationService {

    private final VerificationDao verificationDao;

    private final UserService userService;


    public VerificationManager(VerificationDao verificationDao, UserService userService) {
        this.verificationDao = verificationDao;
        this.userService = userService;
    }

    @Transactional
    @Override
    public Verification createVerification(UUID userId, VerificationType verificationType) {
        var verification = verificationDao.findByUser_IdAndVerificationType(userId, verificationType);
        var user = userService.findById(userId);

        if (user == null) {
                throw new UserNotFoundException(UserMessages.USER_NOT_FOUND);
        }

      if (!verification.isEmpty()){

          var lastVerification = verification.get(0);

          if (lastVerification.getStatus() == VerificationStatus.APPROVED) {
              throw new VerificationAlreadyApprovedException(VerificationMessages.VERIFICATION_ALREADY_APPROVED);
          }

          if (lastVerification.getStatus() == VerificationStatus.PENDING && lastVerification.getExpiryDate().isAfter(LocalDateTime.now())) {
              throw new VerificationAlreadyPendingException(VerificationMessages.VERIFICATION_ALREADY_PENDING);
          }

          if (lastVerification.getStatus() == VerificationStatus.PENDING && lastVerification.getExpiryDate().isBefore(LocalDateTime.now())) {
              lastVerification.setUsed(false);
              lastVerification.setStatus(VerificationStatus.EXPIRED);
              verificationDao.save(lastVerification);
          }
      }

        String sentTo = null;
        switch (verificationType) {
            case EMAIL:
                sentTo = user.getEmail();
                break;
            case PHONE:
                sentTo = user.getPhoneNumber();
                break;
        }

       var newVerification = Verification.builder()
               .user(user)
               .verificationType(verificationType)
               .used(false)
               .verifiedAt(null)
               .sentTo(sentTo)
               .status(VerificationStatus.PENDING)
               .token(generateRandomToken(verificationType))
               .expiryDate(LocalDateTime.now().plusMinutes(15))
               .build();

      return verificationDao.save(newVerification);

    }

    @Override
    public boolean verifyToken(String token) {
        var verification = verificationDao.findByToken(token);

        if (verification.isEmpty()) {
            throw new VerificationTokenNotFoundException(VerificationMessages.VERIFICATION_TOKEN_NOT_FOUND);
        }

        if (verification.get().isUsed() || verification.get().getStatus() == VerificationStatus.INVALIDATED) {
            throw new VerificationTokenAlreadyUsedException(VerificationMessages.VERIFICATION_TOKEN_ALREADY_USED);
        }

        if (verification.get().getExpiryDate().isBefore(LocalDateTime.now()) || verification.get().getStatus() == VerificationStatus.EXPIRED) {
            verification.get().setStatus(VerificationStatus.EXPIRED);
            verificationDao.save(verification.get());
            throw new VerificationTokenExpiredException(VerificationMessages.VERIFICATION_TOKEN_EXPIRED);
        }



        verification.get().setUsed(true);
        verification.get().setStatus(VerificationStatus.APPROVED);
        verification.get().setVerifiedAt(LocalDateTime.now());

        switch (verification.get().getVerificationType()) {
            case EMAIL:
                userService.changeVerificationStatus(verification.get().getUser().getId(), VerificationType.EMAIL, true);
                break;
            case PHONE:
                userService.changeVerificationStatus(verification.get().getUser().getId(), VerificationType.PHONE, true);
                break;
            case IDENTITY:
                userService.changeVerificationStatus(verification.get().getUser().getId(), VerificationType.IDENTITY, true);
                break;
            case DOCUMENT:
                break;
        }
        verificationDao.save(verification.get());
        return true;
    }

    @Override
    public boolean isVerified(User user, VerificationType verificationType) {
        List<Verification> verifications = verificationDao.findByUser_IdAndVerificationTypeAndStatusOrderByCreatedAtDesc(
                user.getId(), verificationType, VerificationStatus.APPROVED);

        if (verifications.isEmpty()) {
            throw new VerificationNotFoundException(VerificationMessages.VERIFICATION_NOT_FOUND);
        }

        Verification lastApproved = verifications.get(0);

        switch (verificationType) {
            case EMAIL:
                return user.getEmail().equals(lastApproved.getSentTo());
            case PHONE:
                return user.getPhoneNumber().equals(lastApproved.getSentTo());
            default:
                return true;
        }
    }

    @Override
    public boolean isValid(String token) {
        var verification = verificationDao.findByToken(token);

        if (verification.isEmpty()) {
            throw new VerificationTokenNotFoundException(VerificationMessages.VERIFICATION_TOKEN_NOT_FOUND);
        }

        if (verification.get().isUsed() || verification.get().getStatus() == VerificationStatus.INVALIDATED) {
            throw new VerificationTokenAlreadyUsedException(VerificationMessages.VERIFICATION_TOKEN_ALREADY_USED);
        }


        if (verification.get().getExpiryDate().isBefore(LocalDateTime.now())) {
            verification.get().setStatus(VerificationStatus.EXPIRED);
            verificationDao.save(verification.get());
            throw new VerificationTokenExpiredException(VerificationMessages.VERIFICATION_TOKEN_EXPIRED);
        }

        return true;
    }

    @Override
    public Verification getVerificationByToken(String token) {
        var verification = verificationDao.findByToken(token);
        if (verification.isEmpty()) {
            throw new VerificationTokenNotFoundException(VerificationMessages.VERIFICATION_TOKEN_NOT_FOUND);
        }

        return verification.get();
    }

    @Override
    public boolean useVerificationToken(String token) {
        var verification = verificationDao.findByToken(token);

        if (verification.isEmpty()) {
            throw new VerificationTokenNotFoundException(VerificationMessages.VERIFICATION_TOKEN_NOT_FOUND);
        }

        if (verification.get().isUsed() || verification.get().getStatus() == VerificationStatus.INVALIDATED) {
            throw new VerificationTokenAlreadyUsedException(VerificationMessages.VERIFICATION_TOKEN_ALREADY_USED);
        }



        if (verification.get().getExpiryDate().isBefore(LocalDateTime.now())) {
            verification.get().setStatus(VerificationStatus.EXPIRED);
            verificationDao.save(verification.get());
            throw new VerificationTokenExpiredException(VerificationMessages.VERIFICATION_TOKEN_EXPIRED);
        }

        verification.get().setUsed(true);
        verification.get().setVerifiedAt(LocalDateTime.now());
        verification.get().setStatus(VerificationStatus.APPROVED);
        verificationDao.save(verification.get());
        return true;
    }

    @Override
    public List<Verification> getVerificationsByUserIdAndType(UUID userId, VerificationType verificationType) {
        var verifications = verificationDao.findByUser_IdAndVerificationType(userId, verificationType);

        if (verifications.isEmpty()) {
            throw new VerificationNotFoundException(VerificationMessages.VERIFICATION_NOT_FOUND);
        }

        return verifications.stream().toList();
    }

    @Override
    public boolean invalidateVerification(UUID id) {
        var verification = verificationDao.findById(id);

        if (verification.isEmpty()) {
            throw new VerificationNotFoundException(VerificationMessages.VERIFICATION_NOT_FOUND);
        }

        verification.get().setStatus(VerificationStatus.INVALIDATED);
        verification.get().setUsed(true);

        verificationDao.save(verification.get());
        return true;

    }


    private String generateRandomToken(VerificationType verificationType){

        SecureRandom random = new SecureRandom();

        if (verificationType == VerificationType.PASSWORD_RESET) {
            byte[] bytes = new byte[16];
            random.nextBytes(bytes);
            return new BigInteger(1, bytes).toString(16).toUpperCase();
        }


        int randomNumber = 10000000 + random.nextInt(90000000);
        return String.valueOf(randomNumber);
    }
}
