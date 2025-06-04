package com.exskylab.koala.business.abstracts;


import com.exskylab.koala.entities.User;
import com.exskylab.koala.entities.VerificationType;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService extends UserDetailsService {

    User findByEmail(String email);

    User findByPhoneNumber(String phoneNumber);

    User findById(UUID id);

    User save(User user);

    boolean existsByEmail(String email);

    boolean existsByPhoneNumber(String phoneNumber);

    List<User> searchByName(String name);

    void delete(UUID id);

    List<User> findAll();

    boolean changeVerificationStatus(UUID id, VerificationType verificationType, boolean status);



}
