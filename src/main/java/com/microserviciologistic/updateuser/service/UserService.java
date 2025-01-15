package com.microserviciologistic.updateuser.service;

import com.microserviciologistic.updateuser.model.User;
import com.microserviciologistic.updateuser.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User updateUser(Long userId, User updatedUser) {
        try {
            // Verificar si el usuario existe
            Optional<User> existingUser = userRepository.findById(userId);
            if (existingUser.isPresent()) {
                User user = existingUser.get();
                // Actualizar los campos necesarios
                user.setName(updatedUser.getName());
                user.setLastname(updatedUser.getLastname());
                user.setEmail(updatedUser.getEmail());
                user.setPhone(updatedUser.getPhone());

                // Guardar los cambios en la base de datos
                return userRepository.save(user);
            } else {
                // Si el usuario no existe, lanzar una excepción 404
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario con id " + userId + " no encontrado");
            }
        } catch (DataAccessException e) {
            // Manejar errores de acceso a la base de datos
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error de conexión a la base de datos: " + e.getMessage(), e);
        }
    }

}
