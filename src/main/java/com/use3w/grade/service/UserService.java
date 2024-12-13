package com.use3w.grade.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.use3w.grade.infra.http.OAuthUserRequest;
import com.use3w.grade.model.UndeterminedUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private OAuthUserRequest request;

    @Autowired
    private TokenService tokenService;

    public UndeterminedUser fetchUndeterminedUserByHeader(String authHeader) {
        String token = tokenService.getTokenValue(authHeader);
        String auth = request.getUserInfo(token);
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(auth, UndeterminedUser.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}
