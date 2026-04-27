package com.example.vinyl_record_collection_tracker.repositories;

import com.example.vinyl_record_collection_tracker.models.PasswordResetToken;
import com.example.vinyl_record_collection_tracker.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    Optional<PasswordResetToken> findByToken(String token);
    void deleteAllByUser(User user);
}
