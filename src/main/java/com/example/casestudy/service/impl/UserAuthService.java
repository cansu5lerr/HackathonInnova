package com.example.casestudy.service.impl;

import com.example.casestudy.dto.request.LoginRequest;
import com.example.casestudy.dto.request.ResetPasswordRequest;
import com.example.casestudy.dto.request.SignupRequest;
import com.example.casestudy.dto.response.JwtResponse;
import com.example.casestudy.dto.response.MessageResponse;
import com.example.casestudy.entity.Role;
import com.example.casestudy.entity.TransactionManagement;
import com.example.casestudy.entity.User;
import com.example.casestudy.enums.ERole;
import com.example.casestudy.repository.RoleRepository;
import com.example.casestudy.repository.UserRepository;
import com.example.casestudy.security.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserAuthService  implements UserAuthServiceImpl{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private TransactionManagementService transactionManagementService;



    public MessageResponse resetPassword (String token, ResetPasswordRequest resetPasswordRequest) {

        User user = userDetailsService.getAuthenticatedUserFromToken(token);
        user.setPassword(encoder.encode(resetPasswordRequest.getResetPassword()));
        userRepository.save(user);
        return new MessageResponse("Password successfully reset!");

    }


    public ResponseEntity<?> authenticateUser(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
                .collect(Collectors.toList());
        JwtResponse jwtResponse = JwtResponse.builder()
                .token(jwt).id(userDetails.getId()).username(userDetails.getUsername()).email(userDetails.getEmail()).roles(roles)
                .build();
        TransactionManagement management = new TransactionManagement();
        management.setMessage(loginRequest.getUsername() + " isimli kullanıcı giriş yaptı.");
        management.setDateTime(LocalDateTime.now());
        transactionManagementService.saveTransactionManagment(management);
        return ResponseEntity.ok().body(jwtResponse);
    }

    public ResponseEntity<?> registerUser(SignupRequest signupRequest) {
        if (userRepository.existsByUsername(signupRequest.getUsername())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
        }

        if (userRepository.existsByEmail(signupRequest.getEmail())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
        }

        User user = User.builder()
                .email(signupRequest.getEmail())
                .username(signupRequest.getUsername())
                .password(encoder.encode(signupRequest.getPassword()))
                .build();

        Set<String> strRoles = signupRequest.getRole();
        Set<Role> roles = new HashSet<>();
        if (strRoles == null) {
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);
                        break;
                    case "mod":
                        Role modRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(modRole);
                        break;
                    default:
                        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                }
            });
        }
        TransactionManagement management = new TransactionManagement();
        management.setMessage(signupRequest.getUsername() + " isimli yeni kullanıcı kaydedildi.");
        management.setDateTime(LocalDateTime.now());
        transactionManagementService.saveTransactionManagment(management);
        user.setRoles(roles);
        userRepository.save(user);
        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }


}
