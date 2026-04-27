package com.example.vinyl_record_collection_tracker.services;

import com.example.vinyl_record_collection_tracker.dtos.UserRequestDTO;
import com.example.vinyl_record_collection_tracker.dtos.UserResponseDTO;
import com.example.vinyl_record_collection_tracker.models.User;
import com.example.vinyl_record_collection_tracker.repositories.UserRepository;
import com.example.vinyl_record_collection_tracker.security.AuthUtil;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final AuthUtil authUtil;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder, AuthUtil authUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authUtil = authUtil;
    }

    private UserResponseDTO toDTO(User user) {
        return new UserResponseDTO(user.getId(), user.getName(), user.getEmail());
    }

    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::toDTO)
                .toList();
    }

    public UserResponseDTO getUserById(Long id) {
        User currentUser = authUtil.getCurrentUser();

        if (!currentUser.getId().equals(id)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can only view your own account.");
        }

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found."));
        return toDTO(user);
    }

    public UserResponseDTO createUser(UserRequestDTO dto) {
        if (userRepository.findByEmail(dto.getEmail()) != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "An account with this email already exists.");
        }

        if (dto.getPassword().length() < 8 ) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password must be at least 8 characters long.");
        }

        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        return toDTO(userRepository.save(user));
    }

    public UserResponseDTO updateUser(Long id, UserRequestDTO dto) {
        User currentUser = authUtil.getCurrentUser();

        if (!currentUser.getId().equals(id)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can only update your own account.");
        }

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found."));

        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        return toDTO(userRepository.save(user));
    }

    public void deleteUser(Long id) {
        User currentUser = authUtil.getCurrentUser();

        if (!currentUser.getId().equals(id)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can only delete your own account.");
        }

        userRepository.deleteById(id);
    }
}