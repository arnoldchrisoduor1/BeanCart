// package com.yourcompany.app.application.service;

// import com.yourcompany.app.application.dto.LoginRequest;
// import com.yourcompany.app.application.dto.LoginResponse;
// import com.yourcompany.app.application.dto.RegisterRequest;
// import com.yourcompany.app.application.dto.UserProfileDto;
// import com.yourcompany.app.common.exception.AuthenticationException;
// import com.yourcompany.app.domain.model.User;
// import com.yourcompany.app.domain.repository.UserRepository;
// import com.yourcompany.app.infrastructure.security.JwtUtil;
// import com.yourcompany.app.infrastructure.security.PasswordEncoder;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.api.extension.ExtendWith;
// import org.mockito.InjectMocks;
// import org.mockito.Mock;
// import org.mockito.junit.jupiter.MockitoExtension;

// import java.util.Optional;
// import java.util.UUID;

// import static org.junit.jupiter.api.Assertions.*;
// import static org.mockito.ArgumentMatchers.any;
// import static org.mockito.ArgumentMatchers.anyString;
// import static org.mockito.Mockito.*;

// @ExtendWith(MockitoExtension.class)
// public class UserServiceTest {

//     @Mock
//     private UserRepository userRepository;

//     @Mock
//     private PasswordEncoder passwordEncoder;

//     @Mock
//     private JwtUtil jwtUtil;

//     @InjectMocks
//     private UserService userService;

//     private User testUser;
//     private UUID userId;

//     @BeforeEach
//     public void setup() {
//         userId = UUID.randomUUID();
//         testUser = new User("test@example.com", "encodedPassword");
//         testUser.setId(userId);
//         testUser.setFirstName("Test");
//         testUser.setLastName("User");
//     }

//     @Test
//     public void testRegisterSuccess() {
//         // Given
//         RegisterRequest request = new RegisterRequest();
//         request.setEmail("test@example.com");
//         request.setPassword("password123");
//         request.setConfirmPassword("password123");

//         when(userRepository.existsByEmail(anyString())).thenReturn(false);
//         when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
//         when(userRepository.save(any(User.class))).thenReturn(testUser);

//         // When
//         UUID result = userService.register(request);

//         // Then
//         assertNotNull(result);
//         verify(userRepository).existsByEmail("test@example.com");
//         verify(passwordEncoder).encode("password123");
//         verify(userRepository).save(any(User.class));
//     }

//     @Test
//     public void testRegisterPasswordMismatch() {
//         // Given
//         RegisterRequest request = new RegisterRequest();
//         request.setEmail("test@example.com");
//         request.setPassword("password123");
//         request.setConfirmPassword("differentPassword");

//         // When & Then
//         assertThrows(AuthenticationException.class, () -> userService.register(request));
//         verify(userRepository, never()).save(any(User.class));
//     }

//     @Test
//     public void testRegisterEmailExists() {
//         // Given
//         RegisterRequest request = new RegisterRequest();
//         request.setEmail("existing@example.com");
//         request.setPassword("password123");
//         request.setConfirmPassword("password123");

//         when(userRepository.existsByEmail("existing@example.com")).thenReturn(true);

//         // When & Then
//         assertThrows(AuthenticationException.class, () -> userService.register(request));
//         verify(userRepository).existsByEmail("existing@example.com");
//         verify(userRepository, never()).save(any(User.class));
//     }

//     @Test
//     public void testLoginSuccess() {
//         // Given
//         LoginRequest request = new LoginRequest();
//         request.setEmail("test@example.com");
//         request.setPassword("password123");

//         when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(testUser));
//         when(passwordEncoder.matches("password123", "encodedPassword")).thenReturn(true);
//         when(jwtUtil.generateToken(anyString(), anyString())).thenReturn("jwt-token");

//         // When
//         LoginResponse response = userService.login(request);

//         // Then
//         assertNotNull(response);
//         assertEquals(testUser.getId(), response.getUserId());
//         assertEquals(testUser.getEmail(), response.getEmail());
//         assertEquals("jwt-token", response.getToken());
//         verify(userRepository).findByEmail("test@example.com");
//         verify(passwordEncoder).matches("password123", "encodedPassword");
//         verify(jwtUtil).generateToken(testUser.getId().toString(), testUser.getEmail());
//     }

//     @Test
//     public void testLoginUserNotFound() {
//         // Given
//         LoginRequest request = new LoginRequest();
//         request.setEmail("nonexistent@example.com");
//         request.setPassword("password123");

//         when(userRepository.findByEmail("nonexistent@example.com")).thenReturn(Optional.empty());

//         // When & Then
//         assertThrows(AuthenticationException.class, () -> userService.login(request));
//         verify(userRepository).findByEmail("nonexistent@example.com");
//         verify(passwordEncoder, never()).matches(anyString(), anyString());
//     }

//     @Test
//     public void testLoginInvalidPassword() {
//         // Given
//         LoginRequest request = new LoginRequest();
//         request.setEmail("test@example.com");
//         request.setPassword("wrongPassword");

//         when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(testUser));
//         when(passwordEncoder.matches("wrongPassword", "encodedPassword")).thenReturn(false);

//         // When & Then
//         assertThrows(AuthenticationException.class, () -> userService.login(request));
//         verify(userRepository).findByEmail("test@example.com");
//         verify(passwordEncoder).matches("wrongPassword", "encodedPassword");
//         verify(jwtUtil, never()).generateToken(anyString(), anyString());
//     }

//     @Test
//     public void testGetUserProfile() {
//         // Given
//         when(userRepository.findById(userId)).thenReturn(Optional.of(testUser));

//         // When
//         UserProfileDto profile = userService.getUserProfile(userId);

//         // Then
//         assertNotNull(profile);
//         assertEquals(userId, profile.getId());
//         assertEquals("test@example.com", profile.getEmail());
//         assertEquals("Test", profile.getFirstName());
//         assertEquals("User", profile.getLastName());
//         verify(userRepository).findById(userId);
//     }

//     @Test
//     public void testUpdateUserProfile() {
//         // Given
//         UserProfileDto profileDto = new UserProfileDto();
//         profileDto.setFirstName("Updated");
//         profileDto.setLastName("Name");

//         when(userRepository.findById(userId)).thenReturn(Optional.of(testUser));

//         // When
//         userService.updateUserProfile(userId, profileDto);

//         // Then
//         assertEquals("Updated", testUser.getFirstName());
//         assertEquals("Name", testUser.getLastName());
//         verify(userRepository).findById(userId);
//         verify(userRepository).save(testUser);
//     }

//     @Test
//     public void testDeleteUser() {
//         // Given
//         when(userRepository.findById(userId)).thenReturn(Optional.of(testUser));

//         // When
//         userService.deleteUser(userId);

//         // Then
//         assertFalse(testUser.isActive());
//         verify(userRepository).findById(userId);
//         verify(userRepository).save(testUser);
//     }
// }