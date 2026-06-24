package com.example.vinyl_record_collection_tracker.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class DemoAccountRestrictedException extends ResponseStatusException {
    public DemoAccountRestrictedException() {
        super(HttpStatus.FORBIDDEN,
                "Demo accounts can not make changes. Sign up for your own account to try this out!");
    }
}