package com.use3w.grade.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.use3w.grade.infra.http.OAuthUserRequest;
import com.use3w.grade.model.UndeterminedUser;
import jakarta.validation.ValidationException;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final OAuthUserRequest request;

    private final TokenService tokenService;

    public UserService(OAuthUserRequest request, TokenService tokenService) {
        this.request = request;
        this.tokenService = tokenService;
    }

    public UndeterminedUser fetchUndeterminedUserByHeader(String authHeader) {
        String token = tokenService.getTokenValue(authHeader);
        String auth = request.getUserInfo(token);
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(auth, UndeterminedUser.class);
        } catch (JsonProcessingException e) {
            throw new ValidationException(e.getLocalizedMessage());
        }
    }

}
