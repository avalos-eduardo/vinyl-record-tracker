package com.example.vinyl_record_collection_tracker.security;

import com.example.vinyl_record_collection_tracker.models.User;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuthUtil {
    public User getCurrentUser(){
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
