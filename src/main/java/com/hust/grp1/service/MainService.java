package com.hust.grp1.service;

import com.hust.grp1.dto.user.UserCreateDto;
import com.hust.grp1.entity.User;
import com.hust.grp1.repository.UserRepository;
import com.hust.grp1.dto.factory.DtoFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class MainService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void register(UserCreateDto newUser){
        User user = DtoFactory.createOriginalObject(newUser);
        user.setRole("ROLE_USER");
        user.setPassword(passwordEncoder.encode(newUser.getPlainTextPassword()));

        userRepository.save(user);
    }
}
