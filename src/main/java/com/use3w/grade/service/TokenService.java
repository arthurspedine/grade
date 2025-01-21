package com.use3w.grade.service;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

@Service
public class TokenService {

    public String getTokenValue(String authHeader) {
        if (!authHeader.startsWith("Bearer"))
            throw new BadCredentialsException("Invalid token");
        return authHeader.replace("Bearer ", "");
    }
}
