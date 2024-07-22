package com.example.casestudy.controller;
import com.example.casestudy.dto.request.LoginRequest;
import com.example.casestudy.dto.request.SignupRequest;
import com.example.casestudy.service.impl.UserAuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth/users")
public class UserController {

    @Autowired
    UserAuthService userAuthService;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        return userAuthService.authenticateUser(loginRequest);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signupRequest) {
        return userAuthService.registerUser(signupRequest);
    }


}