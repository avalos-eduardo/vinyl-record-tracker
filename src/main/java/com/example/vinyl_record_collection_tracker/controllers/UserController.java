package com.example.vinyl_record_collection_tracker.controllers;

import com.example.vinyl_record_collection_tracker.dtos.UserRequestDTO;
import com.example.vinyl_record_collection_tracker.dtos.UserResponseDTO;
import com.example.vinyl_record_collection_tracker.models.User;
import com.example.vinyl_record_collection_tracker.services.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<UserResponseDTO> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public UserResponseDTO getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @PostMapping
    public UserResponseDTO createUser(@RequestBody UserRequestDTO dto) {
        return userService.createUser(dto);
    }

    @PutMapping("/{id}")
    public UserResponseDTO updateUser(@PathVariable Long id, @RequestBody UserRequestDTO dto) {
        return userService.updateUser(id, dto);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }
}