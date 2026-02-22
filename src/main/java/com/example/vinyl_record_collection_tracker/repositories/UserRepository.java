package com.example.vinyl_record_collection_tracker.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.vinyl_record_collection_tracker.models.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
}
