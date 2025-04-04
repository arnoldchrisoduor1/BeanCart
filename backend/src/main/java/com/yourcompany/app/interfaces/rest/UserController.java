package com.yourcompany.app.interfaces.rest;

import com.yourcompany.app.application.dto.UserProfileDto;
import com.yourcompany.app.application.service.UserService;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


@RestController
@RequestMapping("/api/users")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/profile")
    public ResponseEntity<UserProfileDto> getProfile(HttpServletRequest request) {
        UUID userId = (UUID) request.getAttribute("userId");
        System.out.println("Controller: Found user - getting profile: " + userId);
        UserProfileDto profile = userService.getUserProfile(userId);
        System.out.println("Controller: Returning profile");
        return ResponseEntity.ok(profile);
    }

    @PutMapping("/profile")
    public ResponseEntity<Map<String, String>> updateProfile(
        HttpServletRequest request,
        @RequestBody UserProfileDto profileDto
    ) {
        UUID userId = (UUID) request.getAttribute("userId");
        logger.debug("Retrieved userId from request attributes: {}", userId);
        System.out.println("Controller: Extracted userId from token: " + userId);
        userService.updateUserProfile(userId, profileDto);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Profile updated successfully");
        System.out.println("Controller: Profile Updated");

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/profile")
    public ResponseEntity<Map<String, String>> deleteProfile(HttpServletRequest request) {
        UUID userId = (UUID) request.getAttribute("userId");
        userService.deleteUser(userId);

        Map<String, String> response = new HashMap<>();
        response.put("message", "User account deactivated successfully");
        System.out.println("Controller: Profile Deleted");

        return ResponseEntity.ok(response);
    }

}
