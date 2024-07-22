package com.example.casestudy.service.impl;

import com.example.casestudy.dto.response.UserDetailsResponse;
import com.example.casestudy.entity.User;
import com.example.casestudy.exception.UserException;
import com.example.casestudy.repository.UserRepository;
import com.example.casestudy.security.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    private JwtUtils jwtUtils;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));

        return UserDetailsImpl.build(user);
    }
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User getUser(Long userId) {
        return userRepository.findById(userId).orElse(null);
    }

    public User getAuthenticatedUserFromToken(String token) {
        String username = jwtUtils.extractUsername(token);
        UserDetails userDetails = null;

        if (username != null) {
            userDetails = loadUserByUsername(username);
            Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
            Optional<User> userOptional = userRepository.findByUsername(username);

            if (userOptional.isPresent()) {
                return userOptional.get();
            }
        }
        throw new UserException("User not found");
    }
    public UserDetailsResponse updateUserProfile(String token , UserDetailsResponse userDetailsResponse) {
        String username = jwtUtils.extractUsername(token);
        UserDetails userDetails = null;
        if(username != null) {
            userDetails = loadUserByUsername(username);
            Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
            Optional<User> userOptional = userRepository.findByUsername(username);
            if(userOptional.isPresent()){
                User user = userOptional.get();
                user.setId(user.getId());
                user.setPassword(user.getPassword());
                user.setUsername(user.getUsername());
                user.setEmail(userDetailsResponse.getEmail());
                user.setRoles(user.getRoles());
                userRepository.save(user);
            }
        }
        return userDetailsResponse;
    }

}