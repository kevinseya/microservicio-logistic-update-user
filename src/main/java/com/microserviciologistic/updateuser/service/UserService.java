package com.microserviciologistic.updateuser.service;

import com.microserviciologistic.updateuser.model.User;
import com.microserviciologistic.updateuser.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final WebSocketClientService webSocketClientService;


    @Autowired
    public UserService(UserRepository userRepository, WebSocketClientService webSocketClientService ,PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.webSocketClientService = webSocketClientService;

    }

    public User updateUser(UUID userId, User updatedUser) {
        try {
            Optional<User> existingUserOptional = userRepository.findById(userId);
            if (existingUserOptional.isPresent()) {
                User user = existingUserOptional.get();

                user.setName(updatedUser.getName());
                user.setLastname(updatedUser.getLastname());
                user.setEmail(updatedUser.getEmail());
                user.setPhone(updatedUser.getPhone());
                user.setRole(updatedUser.getRole());


                if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
                    user.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
                }
                User editUser = userRepository.save(user);
                System.out.println("Enviando evento WebSocket para actualización de usuario...");
                webSocketClientService.sendEvent("UPDATE", editUser);
                return editUser;
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User with id " + userId + " not found");
            }
        } catch (DataAccessException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error connecting with database: " + e.getMessage(), e);
        }
    }
}
