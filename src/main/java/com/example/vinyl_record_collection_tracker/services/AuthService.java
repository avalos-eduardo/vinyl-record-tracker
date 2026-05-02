package com.example.vinyl_record_collection_tracker.services;

import com.example.vinyl_record_collection_tracker.dtos.AuthResponseDTO;
import com.example.vinyl_record_collection_tracker.dtos.LoginRequestDTO;
import com.example.vinyl_record_collection_tracker.models.PasswordResetToken;
import com.example.vinyl_record_collection_tracker.models.User;
import com.example.vinyl_record_collection_tracker.repositories.PasswordResetTokenRepository;
import com.example.vinyl_record_collection_tracker.repositories.UserRepository;
import com.example.vinyl_record_collection_tracker.security.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final EmailService emailService;
    private final BCryptPasswordEncoder passwordEncoder;

    public AuthService(AuthenticationManager authenticationManager, JwtUtil jwtUtil, UserRepository userRepository, PasswordResetTokenRepository passwordResetTokenRepository, EmailService emailService, BCryptPasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
        this.passwordResetTokenRepository = passwordResetTokenRepository;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
    }

    public void login(LoginRequestDTO dto, HttpServletResponse response) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword())
            );

            String email = authentication.getName();
            String token = jwtUtil.generateToken(email);

            Cookie cookie = new Cookie("token", token);
            cookie.setHttpOnly(true);
            cookie.setSecure(false); // set to true when ready for deployment (requires https)
            cookie.setPath("/");
            cookie.setMaxAge(86400); // 24 hours
            response.addCookie(cookie);

        } catch (AuthenticationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid email or password.");
        }
    }

    public void logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("token", "");
        cookie.setHttpOnly(true);
        cookie.setSecure(false); // set to true when ready for deployment (requires https)
        cookie.setPath("/");
        cookie.setMaxAge(0); // immediately expire the cookie
        response.addCookie(cookie);
    }

    public void forgotPassword(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            return;
        }

        String token = UUID.randomUUID().toString();

        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setToken(token);
        resetToken.setUser(user);
        resetToken.setUsed(false);
        resetToken.setExpiresAt(LocalDateTime.now().plusMinutes(30));
        passwordResetTokenRepository.save(resetToken);

        emailService.sendPasswordResetEmail(email, token);
    }

    @Transactional
    public void resetPassword(String token, String newPassword) {
        PasswordResetToken resetToken = passwordResetTokenRepository.findByToken(token).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid or expired reset token."));

        if (resetToken.isUsed()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Reset token has already been used.");
        }

        if (resetToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Reset token has expired.");
        }

        User user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        passwordResetTokenRepository.deleteAllByUser(user);
    }
}
