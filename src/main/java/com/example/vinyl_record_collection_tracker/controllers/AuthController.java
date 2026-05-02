package com.example.vinyl_record_collection_tracker.controllers;

import com.example.vinyl_record_collection_tracker.dtos.AuthResponseDTO;
import com.example.vinyl_record_collection_tracker.dtos.ForgotPasswordRequestDTO;
import com.example.vinyl_record_collection_tracker.dtos.LoginRequestDTO;
import com.example.vinyl_record_collection_tracker.dtos.ResetPasswordRequestDTO;
import com.example.vinyl_record_collection_tracker.services.AuthService;
import jakarta.servlet.http.HttpServletResponse;
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
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void login(@RequestBody LoginRequestDTO dto, HttpServletResponse response) {
        authService.login(dto, response);
    }

    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void logout(HttpServletResponse response) {
        authService.logout(response);
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
