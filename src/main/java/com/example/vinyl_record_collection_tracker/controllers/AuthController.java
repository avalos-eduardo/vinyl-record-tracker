package com.example.vinyl_record_collection_tracker.controllers;

import com.example.vinyl_record_collection_tracker.dtos.AuthResponseDTO;
import com.example.vinyl_record_collection_tracker.dtos.ForgotPasswordRequestDTO;
import com.example.vinyl_record_collection_tracker.dtos.LoginRequestDTO;
import com.example.vinyl_record_collection_tracker.dtos.ResetPasswordRequestDTO;
import com.example.vinyl_record_collection_tracker.services.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public AuthResponseDTO login(@RequestBody LoginRequestDTO dto) {
        return authService.login(dto);
    }

    @PostMapping("/forgot-password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void forgotPassword(@RequestBody ForgotPasswordRequestDTO dto) {
        authService.forgotPassword(dto.getEmail());
    }

    @PostMapping("/reset-password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void resetPassword(@RequestBody ResetPasswordRequestDTO dto) {
        authService.resetPassword(dto.getToken(), dto.getNewPassword());
    }
}
