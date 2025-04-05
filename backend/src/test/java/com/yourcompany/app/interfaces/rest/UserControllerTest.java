package com.yourcompany.app.interfaces.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yourcompany.app.application.dto.UserProfileDto;
import com.yourcompany.app.application.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    private final UUID TEST_USER_ID = UUID.randomUUID();

    private RequestPostProcessor userIdAttribute() {
        return request -> {
            request.setAttribute("userId", TEST_USER_ID);
            request.setAttribute("email", "test@example.com");
            return request;
        };
    }

    @Test
    public void testGetProfile() throws Exception {
        // Given
        UserProfileDto profile = new UserProfileDto();
        profile.setId(TEST_USER_ID);
        profile.setEmail("test@example.com");
        profile.setFirstName("Test");
        profile.setLastName("User");

        when(userService.getUserProfile(TEST_USER_ID)).thenReturn(profile);

        // When & Then
        mockMvc.perform(get("/api/users/profile")
                .with(userIdAttribute()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(TEST_USER_ID.toString()))
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.firstName").value("Test"))
                .andExpect(jsonPath("$.lastName").value("User"));
    }

    @Test
    public void testUpdateProfile() throws Exception {
        // Given
        UserProfileDto profileDto = new UserProfileDto();
        profileDto.setFirstName("Updated");
        profileDto.setLastName("Name");

        doNothing().when(userService).updateUserProfile(eq(TEST_USER_ID), any(UserProfileDto.class));

        // When & Then
        mockMvc.perform(put("/api/users/profile")
                .with(userIdAttribute())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(profileDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Profile updated successfully"));
    }

    @Test
    public void testDeleteProfile() throws Exception {
        // Given
        doNothing().when(userService).deleteUser(TEST_USER_ID);

        // When & Then
        mockMvc.perform(delete("/api/users/profile")
                .with(userIdAttribute()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("User account deactivated successfully"));
    }
}