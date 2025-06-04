package com.exskylab.koala.business.concretes;

import com.exskylab.koala.business.abstracts.UserService;
import com.exskylab.koala.business.abstracts.VerificationService;
import com.exskylab.koala.core.constants.messages.VerificationMessages;
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
    public Verification createVerification(User user, VerificationType verificationType) {
        var verification = verificationDao.findByUserAndVerificationType(user, verificationType);

      if (verification.isPresent()){

          if (verification.get().getStatus() == VerificationStatus.APPROVED) {
              throw new IllegalStateException(VerificationMessages.VERIFICATION_ALREADY_APPROVED);
          }

          if (verification.get().getStatus() == VerificationStatus.PENDING && verification.get().getExpiryDate().isAfter(LocalDateTime.now())) {
              throw new IllegalStateException(VerificationMessages.VERIFICATION_ALREADY_PENDING);
          }

          if (verification.get().getStatus() == VerificationStatus.PENDING && verification.get().getExpiryDate().isBefore(LocalDateTime.now())) {
              verification.get().setUsed(false);
              verification.get().setStatus(VerificationStatus.EXPIRED);
              verificationDao.save(verification.get());
          }
      }

       var newVerification = Verification.builder()
               .user(user)
               .verificationType(verificationType)
               .used(false)
               .verifiedAt(null)
               .status(VerificationStatus.PENDING)
               .token(generateRandomToken())
               .expiryDate(LocalDateTime.now().plusMinutes(15))
               .build();

      return verificationDao.save(newVerification);

    }

    @Override
    public boolean verifyToken(String token) {
        var verification = verificationDao.findByToken(token);

        if (verification.isEmpty()) {
            throw new IllegalArgumentException(VerificationMessages.VERIFICATION_TOKEN_NOT_FOUND);
        }

        if (verification.get().isUsed()) {
            throw new IllegalStateException(VerificationMessages.VERIFICATION_TOKEN_ALREADY_USED);
        }

        if (verification.get().getExpiryDate().isBefore(LocalDateTime.now()) || verification.get().getStatus() == VerificationStatus.EXPIRED) {
            verification.get().setStatus(VerificationStatus.EXPIRED);
            verificationDao.save(verification.get());
            throw new IllegalStateException(VerificationMessages.VERIFICATION_TOKEN_EXPIRED);
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
        var verification = verificationDao.findByUserAndVerificationType(user, verificationType);

        if (verification.isEmpty()) {
            return false;
        }

        if (verification.get().getStatus() == VerificationStatus.APPROVED) {
            return true;
        }

        if (verification.get().getStatus() == VerificationStatus.PENDING && verification.get().getExpiryDate().isAfter(LocalDateTime.now())) {
            return false;
        }

        return false;
    }


    private String generateRandomToken(){
        SecureRandom random = new SecureRandom();
        int randomNumber = 10000000 + random.nextInt(90000000);
        return String.valueOf(randomNumber);
    }
}
