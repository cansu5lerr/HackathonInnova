package com.example.casestudy.service.impl;

import com.example.casestudy.dto.request.LoginRequest;
import com.example.casestudy.dto.request.SignupRequest;
import org.springframework.http.ResponseEntity;

public interface UserAuthServiceImpl {
    ResponseEntity<?> authenticateUser(LoginRequest loginRequest);
    ResponseEntity<?> registerUser(SignupRequest signupRequest);

}
