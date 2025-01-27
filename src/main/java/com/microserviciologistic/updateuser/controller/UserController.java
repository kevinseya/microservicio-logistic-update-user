package com.microserviciologistic.updateuser.controller;

import com.microserviciologistic.updateuser.model.User;
import com.microserviciologistic.updateuser.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@Tag(name = "Users", description = "Endpoints for managing users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PutMapping("/update/{id}")
    @Operation(summary = "Update user", description = "Endpoint to update user information.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User updated successfully"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "500", description = "Server error")
    })
    public ResponseEntity<User> updateUser(@Valid @PathVariable UUID id, @RequestBody User updatedUser) {
        try {
            System.out.println("Updating user with ID: " + id);
            User user = userService.updateUser(id, updatedUser);
            return ResponseEntity.ok(user);
        } catch (ResponseStatusException e) {
            System.err.println("Error updating user: " + e.getMessage());
            throw e; // Propagar ResponseStatusException para mantener los códigos HTTP personalizados
        } catch (Exception e) {
            System.err.println("Error unexpected: " + e.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error updating user: " + e.getMessage(), e
            );
        }
    }
}
