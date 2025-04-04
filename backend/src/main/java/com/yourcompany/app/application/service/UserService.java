package com.yourcompany.app.application.service;

import com.yourcompany.app.application.dto.LoginRequest;
import com.yourcompany.app.application.dto.LoginResponse;
import com.yourcompany.app.application.dto.RegisterRequest;
import com.yourcompany.app.application.dto.UserProfileDto;
import com.yourcompany.app.common.exception.AuthenticationException;
import com.yourcompany.app.domain.model.User;
import com.yourcompany.app.domain.repository.UserRepository;
import com.yourcompany.app.infrastructure.security.JwtUtil;
import com.yourcompany.app.infrastructure.security.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class UserService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }
    
    @Transactional
    public UUID register(RegisterRequest request) {
        // Check if passwords match
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new AuthenticationException("Passwords do not match");
        }
        
        // Check if user already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new AuthenticationException("Email already in use");
        }
        
        // Create new user
        User user = new User(request.getEmail(), passwordEncoder.encode(request.getPassword()));
        
        // Save user
        userRepository.save(user);
        
        return user.getId();
    }
    
    public LoginResponse login(LoginRequest request) {
        // Find user by email
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new AuthenticationException("Invalid email or password"));
        
        // Check password
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new AuthenticationException("Invalid email or password");
        }
        
        // Generate JWT token
        String token = jwtUtil.generateToken(user.getId().toString(), user.getEmail());
        
        return new LoginResponse(user.getId(), user.getEmail(), token);
    }
    
    public UserProfileDto getUserProfile(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AuthenticationException("User not found"));
        
        UserProfileDto profile = new UserProfileDto();
        profile.setId(user.getId());
        profile.setEmail(user.getEmail());
        profile.setFirstName(user.getFirstName());
        profile.setLastName(user.getLastName());
        profile.setCreatedAt(user.getCreatedAt());
        
        return profile;
    }
    
    @Transactional
    public void updateUserProfile(UUID userId, UserProfileDto profileDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AuthenticationException("User not found"));
        
        if (profileDto.getFirstName() != null) {
            user.setFirstName(profileDto.getFirstName());
        }
        
        if (profileDto.getLastName() != null) {
            user.setLastName(profileDto.getLastName());
        }
        
        userRepository.save(user);
    }
    
    @Transactional
    public void deleteUser(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AuthenticationException("User not found"));
        
        user.setActive(false);
        userRepository.save(user);
    }
}