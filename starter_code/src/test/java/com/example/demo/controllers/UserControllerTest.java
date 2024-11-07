package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static com.example.demo.TestUtils.createUser;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class UserControllerTest {

    private UserController userController;
    private UserRepository userRepository = mock(UserRepository.class);
    private CartRepository cartRepository = mock(CartRepository.class);

    private BCryptPasswordEncoder bCryptPasswordEncoder = mock(BCryptPasswordEncoder.class);

    @Before
    public void setUp(){
        userController = new UserController();
        TestUtils.injectObject(userController,"userRepository",userRepository);
        TestUtils.injectObject(userController,"cartRepository",cartRepository);
        TestUtils.injectObject(userController,"bCryptPasswordEncoder",bCryptPasswordEncoder);
        when(userRepository.findById(1L)).thenReturn(Optional.of(createUser()));

        when(userRepository.findByUsername("test")).thenReturn(createUser());

    }
    @Test
    public void create_user_happy_path(){
        when(bCryptPasswordEncoder.encode("testPassword")).thenReturn("thisIsHashsed");
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername("test");
        createUserRequest.setPassword("testPassword");
        createUserRequest.setConfirmPassword("testPassword");
        final ResponseEntity<User> response = userController.createUser(createUserRequest);

        assertNotNull(response);
        assertEquals(200,response.getStatusCodeValue());

        User user = response.getBody();
        assertNotNull(user);
        assertEquals(0, user.getId());
        assertEquals("test", user.getUsername());
        assertEquals("thisIsHashsed",user.getPassword());
    }

    @Test
    public void testRetrieveUserById(){
        Long id = 1L;
        ResponseEntity<User> responseEntity = userController.findById(id);
        assertNotNull(responseEntity);
        assertEquals(200, responseEntity.getStatusCodeValue());
        User user2 = responseEntity.getBody();
        assertNotNull(user2);
        assertEquals(id, Long.valueOf(user2.getId()));
        assertEquals("test", user2.getUsername());
        assertEquals("testPassword", user2.getPassword());
    }
    @Test
    public void testRetrieveUserByName(){
        ResponseEntity<User> responseEntity = userController.findByUserName("test");
        assertNotNull(responseEntity);
        assertEquals(200, responseEntity.getStatusCodeValue());
        User user2 = responseEntity.getBody();
        assertNotNull(user2);
        assertEquals(1L, user2.getId());
        assertEquals("test", user2.getUsername());
        assertEquals("testPassword", user2.getPassword());
    }
}

