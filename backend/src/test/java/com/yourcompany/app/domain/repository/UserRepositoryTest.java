package com.yourcompany.app.domain.repository;

import com.yourcompany.app.domain.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTest {
    
    @Autowired
    private UserRepository userRepository;

    @Test
    public void testFindByEmail() {
        // Given
        String email = "test@example.com";
        User user = new User(email, "password123");
        userRepository.save(user);

        // When
        Optional<User> foundUser = userRepository.findByEmail(email);

        //Then
        assertTrue(foundUser.isPresent());
        assertEquals(email, foundUser.get().getEmail());
    }

    @Test
    public void testExistsByEmail() {
        // Given
        String email = "exists@example.com";
        User user = new User(email, "password123");
        userRepository.save(user);

        //When & Then
        assertTrue(userRepository.existsByEmail(email));
        assertFalse(userRepository.existsByEmail("notexists@example.com"));
    }

}
