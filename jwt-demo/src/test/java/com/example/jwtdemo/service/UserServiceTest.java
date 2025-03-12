package com.example.jwtdemo.service;

import com.example.jwtdemo.exception.EmptyUsernameOrPasswordException;
import com.example.jwtdemo.exception.UsernameAlreadyExistsException;
import com.example.jwtdemo.model.Order;
import com.example.jwtdemo.model.User;
import com.example.jwtdemo.model.UserRole;
import com.example.jwtdemo.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private User mockUser;
    private final String username = "testUser";
    private final String rawPassword = "password123";
    private final UserRole userRole = UserRole.STANDARD;

    @BeforeEach
    void setUp() {
        mockUser = new User();
        mockUser.setUsername(username);
        mockUser.setPassword(rawPassword);
        mockUser.setUserRole(userRole);
    }

    //Register success
    @Test
    void testRegisterUser_Success() {
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());
        when(passwordEncoder.encode(rawPassword)).thenReturn("encodedPassword");

        User registeredUser = userService.registerUser(username, rawPassword, userRole);

        verify(userRepository, times(1)).save(any(User.class));
    }

    //trying to register with empty username and password
    @Test
    void testRegisterUser_EmptyUsernameOrPassword() {
        String emptyUsername = "";
        String emptyPassword = "";

        assertThrows(EmptyUsernameOrPasswordException.class, () -> userService.registerUser(emptyUsername, rawPassword, userRole));
        assertThrows(EmptyUsernameOrPasswordException.class, () -> userService.registerUser(username, emptyPassword, userRole));
    }

    //Trying to register with an existing username
    @Test
    void testRegisterUserUsernameAlreadyExists() {
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(mockUser));
        assertThrows(UsernameAlreadyExistsException.class, () -> userService.validateRegistrationInputs(username, rawPassword));
    }


    //Get all users
    @Test
    void testListAllUsers() {
        User anotherUser = new User();
        anotherUser.setUsername("anotherUser");
        when(userRepository.findAll()).thenReturn(List.of(mockUser, anotherUser));

        List<User> users = userService.listAllUsers();

        assertEquals(2, users.size());
        assertTrue(users.contains(mockUser));
        assertTrue(users.contains(anotherUser));

        verify(userRepository, times(1)).findAll();
    }
}
