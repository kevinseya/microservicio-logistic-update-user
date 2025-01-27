package com.microserviciologistic.updateuser.service;

import com.microserviciologistic.updateuser.model.User;
import com.microserviciologistic.updateuser.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User updateUser(UUID userId, User updatedUser) {
        try {
            Optional<User> existingUser = userRepository.findById(userId);
            if (existingUser.isPresent()) {
                User user = existingUser.get();
                user.setName(updatedUser.getName());
                user.setLastname(updatedUser.getLastname());
                user.setEmail(updatedUser.getEmail());
                user.setPhone(updatedUser.getPhone());

                return userRepository.save(user);
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User with id " + userId + " not found");
            }
        } catch (DataAccessException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error connection with database: " + e.getMessage(), e);
        }
    }

}
