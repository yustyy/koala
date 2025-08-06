package com.exskylab.koala.business.concretes;


import com.exskylab.koala.business.abstracts.UserService;

import com.exskylab.koala.core.dtos.user.CreateUserDto;
import com.exskylab.koala.core.dtos.user.UpdateUserDto;
import com.exskylab.koala.core.exceptions.UserNotFoundException;
import com.exskylab.koala.dataAccess.UserDao;
import com.exskylab.koala.entities.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
        return null;
    }

    @Override
    public List<User> getAll() {
        return List.of();
    }

    @Override
    public User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
            throw new UserNotFoundException("{user.not.authenticated}");
        }

        return (User) authentication.getPrincipal();
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
        return false;
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
    public User getSystemUser() {
        UUID systemUserId = UUID.fromString("00000000-0000-0000-0000-000000000001");

        return userDao.findById(systemUserId).orElseThrow( () ->
                new UserNotFoundException("{system.user.not.found}"));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }
}
