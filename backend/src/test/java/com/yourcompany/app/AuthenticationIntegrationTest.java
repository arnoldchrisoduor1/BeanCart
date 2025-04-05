package com.yourcompany.app;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yourcompany.app.application.dto.LoginRequest;
import com.yourcompany.app.application.dto.RegisterRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.Matchers.hasLength;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.greaterThan;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class AuthenticationIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testCompleteAuthenticationFlow() throws Exception {
        // Step 1: Register a user
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setEmail("integration@test.com");
        registerRequest.setPassword("Password123");
        registerRequest.setConfirmPassword("Password123");

        MvcResult registerResult = mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.userId").exists())
                .andReturn();

        String responseJson = registerResult.getResponse().getContentAsString();
        String userId = objectMapper.readTree(responseJson).get("userId").asText();
        assertNotNull(userId);

        // Step 2: Login with the registered user
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("integration@test.com");
        loginRequest.setPassword("Password123");

        MvcResult loginResult = mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.token").isNotEmpty())
                .andExpect(jsonPath("$.token.length()", greaterThan(10)))
                .andReturn();

        String token = objectMapper.readTree(loginResult.getResponse().getContentAsString())
                .get("token").asText();

        // Step 3: Access protected resource with token
        mockMvc.perform(get("/api/users/profile")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.email").value("integration@test.com"));

        // Step 4: Update profile with token
        mockMvc.perform(put("/api/users/profile")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"firstName\":\"Integration\",\"lastName\":\"Test\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Profile updated successfully"));

        // Step 5: Verify profile was updated
        mockMvc.perform(get("/api/users/profile")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Integration"))
                .andExpect(jsonPath("$.lastName").value("Test"));
    }
}