package com.exskylab.koala.business.concretes;

import com.exskylab.koala.business.abstracts.UserService;
import com.exskylab.koala.core.constants.messages.UserMessages;
import com.exskylab.koala.dataAccess.UserDao;
import com.exskylab.koala.entities.User;
import com.exskylab.koala.entities.VerificationType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserManager implements UserService {

    private final UserDao userDao;

    public UserManager(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public User findByEmail(String email) {
        return null;
    }

    @Override
    public User findByPhoneNumber(String phoneNumber) {
        return null;
    }

    @Override
    public User findById(UUID id) {
        return null;
    }

    @Override
    public User save(User user) {
        return null;
    }

    @Override
    public boolean existsByEmail(String email) {
        return false;
    }

    @Override
    public boolean existsByPhoneNumber(String phoneNumber) {
        return false;
    }

    @Override
    public List<User> searchByName(String name) {
        return List.of();
    }

    @Override
    public void delete(UUID id) {

    }

    @Override
    public List<User> findAll() {
        return List.of();
    }

    @Override
    public boolean changeVerificationStatus(UUID id, VerificationType verificationType, boolean status) {
       User user = userDao.findById(id).orElseThrow(()-> new IllegalArgumentException(UserMessages.USER_NOT_FOUND));

       if(verificationType == null){
              throw new IllegalArgumentException(UserMessages.VERIFICATION_TYPE_NOT_FOUND);
       }

         if(verificationType == VerificationType.EMAIL){
              user.setEmailVerified(status);
         } else if(verificationType == VerificationType.PHONE){
              user.setPhoneVerified(status);
         } else if(verificationType == VerificationType.IDENTITY){
                user.setIdentityVerified(status);
         } else {
             throw new IllegalArgumentException(UserMessages.VERIFICATION_TYPE_NOT_FOUND);
         }

         userDao.save(user);
         return true;

    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }
}
