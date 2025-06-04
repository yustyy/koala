package com.exskylab.koala.business.concretes;

import com.exskylab.koala.business.abstracts.UserService;
import com.exskylab.koala.dataAccess.UserDao;
import com.exskylab.koala.entities.User;
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
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }
}
