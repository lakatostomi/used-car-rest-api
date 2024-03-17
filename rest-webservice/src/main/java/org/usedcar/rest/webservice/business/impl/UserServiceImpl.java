package org.usedcar.rest.webservice.business.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.usedcar.rest.webservice.business.UserService;
import org.usedcar.rest.webservice.dto.RegistrationDTO;
import org.usedcar.rest.webservice.exception.EmailAlreadyExistsException;
import org.usedcar.rest.webservice.model.User;
import org.usedcar.rest.webservice.repository.UserRepository;

import java.util.Date;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public UserServiceImpl(PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public void saveUser(RegistrationDTO registrationDTO) {
        log.info("Saving new User - name: {}, email: {}", registrationDTO.getName(), registrationDTO.getEmail());
        try {
            userRepository.save(new User(registrationDTO.getName(), registrationDTO.getEmail(), passwordEncoder.encode(registrationDTO.getPassword())));
        } catch (DataIntegrityViolationException ex) {
            throw new EmailAlreadyExistsException("An account already exist with this username: " + registrationDTO.getEmail());
        }
    }

    public void logoutUser() {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        log.info("Logout User with username: {}", userName);
        User user = userRepository.findByEmail(userName);
        user.setTimeOfLogout(new Date());
        userRepository.save(user);
        SecurityContextHolder.clearContext();
    }
}
