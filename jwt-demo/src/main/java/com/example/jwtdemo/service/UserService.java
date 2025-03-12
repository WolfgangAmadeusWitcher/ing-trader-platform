package com.example.jwtdemo.service;

import com.example.jwtdemo.exception.EmptyUsernameOrPasswordException;
import com.example.jwtdemo.exception.UsernameAlreadyExistsException;
import com.example.jwtdemo.model.User;
import com.example.jwtdemo.model.UserRole;
import com.example.jwtdemo.repository.UserRepository;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User registerUser(String username, String rawPass, UserRole userRole) {

        validateRegistrationInputs(username, rawPass);

        String encodedPass = passwordEncoder.encode(rawPass);
        User user = new User();
        user.setUsername(username);
        user.setPassword(encodedPass);
        user.setUserRole(userRole);
        return userRepository.save(user);
    }

    public List<User> listAllUsers() {
        return userRepository.findAll();
    }

    public void validateRegistrationInputs(String username, String password) {
        if(StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
            throw new EmptyUsernameOrPasswordException();
        }

        if(userRepository.findByUsername(username).isPresent()) {
            throw new UsernameAlreadyExistsException(username);
        }
    }
}
