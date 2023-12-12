package com.darwgom.userapi.application.usecases.impl;

import com.darwgom.userapi.application.dto.*;
import com.darwgom.userapi.domain.entities.Phone;
import com.darwgom.userapi.domain.entities.Role;
import com.darwgom.userapi.domain.entities.User;
import com.darwgom.userapi.domain.exceptions.UserNotFoundException;
import com.darwgom.userapi.domain.repositories.UserRepository;
import com.darwgom.userapi.domain.repositories.RoleRepository;
import com.darwgom.userapi.infrastucture.security.JwtTokenProvider;

import jakarta.persistence.EntityNotFoundException;

import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import com.darwgom.userapi.domain.enums.RoleNameEnum;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.util.Optional;
@SpringBootTest
class UserUseCaseImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;


    @InjectMocks
    private UserUseCaseImpl userUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void registerUserSuccessfully() {

        UserInputDTO userInputDTO = new UserInputDTO();
        userInputDTO.setEmail("test@example.com");
        userInputDTO.setPassword("passworD1+");
        userInputDTO.setRole("ROLE_ADMIN");

        PhoneInputDTO phone1 = new PhoneInputDTO();
        phone1.setNumber("123456789");
        phone1.setCitycode("1");
        phone1.setCountrycode("57");

        PhoneInputDTO phone2 = new PhoneInputDTO();
        phone2.setNumber("987654321");
        phone2.setCitycode("2");
        phone2.setCountrycode("57");

        Set<PhoneInputDTO> phoneSetInput = new HashSet<>();
        phoneSetInput.add(phone1);
        phoneSetInput.add(phone2);
        userInputDTO.setPhones(phoneSetInput);

        Phone phoneTest1 = new Phone();
        phoneTest1.setNumber("123456789");
        phoneTest1.setCityCode("1");
        phoneTest1.setCountryCode("57");

        Phone phoneTest2 = new Phone();
        phoneTest2.setNumber("987654321");
        phoneTest2.setCityCode("2");
        phoneTest2.setCountryCode("57");

        User user = new User();
        user.setEmail(userInputDTO.getEmail());
        user.setPassword(userInputDTO.getPassword());
        user.setEmail("test@example.com");

        String token = "testToken";
        when(modelMapper.map(any(User.class), eq(UserDTO.class)))
            .thenAnswer(invocation -> {
                User user2 = invocation.getArgument(0);
                UserDTO userDTO = new UserDTO();
                userDTO.setId(user2.getId());
                userDTO.setEmail(user2.getEmail());
                return userDTO;
            });
        Role role = new Role(RoleNameEnum.ROLE_USER);
        when(modelMapper.map(any(UserInputDTO.class), eq(User.class))).thenReturn(user);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(roleRepository.findByName(any())).thenReturn(Optional.of(role));
        when(jwtTokenProvider.createToken(anyString(), anyList())).thenReturn("mockToken");
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(modelMapper.map(any(PhoneInputDTO.class), eq(Phone.class)))
                .thenAnswer(invocation -> {
                    PhoneInputDTO input = invocation.getArgument(0);
                    Phone phone = new Phone();
                    phone.setNumber(input.getNumber());
                    phone.setCityCode(input.getCitycode());
                    phone.setCountryCode(input.getCountrycode());
                    return phone;
                });
        UserDTO result = userUseCase.registerUser(userInputDTO);
        assertNotNull(result);
        assertEquals("mockToken", result.getToken());

        verify(userRepository).save(any(User.class));
    }

    @Test
    void updateUserSuccessfully() {
        UUID userId = UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        UserInputDTO userInputDTO = new UserInputDTO();
        userInputDTO.setEmail("test@example.com");
        userInputDTO.setPassword("passworD1+");
        userInputDTO.setRole("ROLE_ADMIN");
                User user = new User();
        user.setEmail(userInputDTO.getEmail());
        user.setPassword(userInputDTO.getPassword());
        user.setEmail("test@example.com");
        Role role = new Role();role.setName(RoleNameEnum.ROLE_ADMIN);
        PhoneInputDTO phone1 = new PhoneInputDTO();
        phone1.setNumber("123456789");
        phone1.setCitycode("1");
        phone1.setCountrycode("57");

        PhoneInputDTO phone2 = new PhoneInputDTO();
        phone2.setNumber("987654321");
        phone2.setCitycode("2");
        phone2.setCountrycode("57");

        Set<PhoneInputDTO> phoneDTOs = new HashSet<>();
        phoneDTOs.add(phone1);
        phoneDTOs.add(phone2);

        userInputDTO.setPhones(phoneDTOs);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(roleRepository.findByName(any())).thenReturn(Optional.of(role));
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(modelMapper.map(any(User.class), eq(UserDTO.class))).thenReturn(new UserDTO());
        when(modelMapper.map(any(PhoneInputDTO.class), eq(Phone.class))).thenReturn(new Phone());
        UserDTO result = userUseCase.updateUser(userId, userInputDTO);
        assertNotNull(result);
    }

    @Test
    void loginUserSuccessfully() {
        LoginDTO loginDTO = new LoginDTO("user@example.com", "password");
        User user = new User();
        user.setEmail("user@example.com");
        user.setRole(new Role(RoleNameEnum.ROLE_USER));
        user.setToken(null);

        Authentication authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(authentication.getName()).thenReturn("user@example.com");
        when(authentication.getAuthorities()).thenReturn(Collections.emptyList());
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));
        when(jwtTokenProvider.createToken(anyString(), anyList())).thenReturn("newToken");

        TokenDTO result = userUseCase.loginUser(loginDTO);

        assertNotNull(result);
        assertNotNull(result.getToken());
        assertEquals("newToken", result.getToken());
        verify(userRepository, times(2)).save(any(User.class));
    }

    @Test
    void deleteUserSuccessfully() {
        UUID userId =  UUID.fromString("c2c911d8-d79d-4285-b2c5-f0436ebdde72");
        User user = new User();
        user.setId(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        UserDeleteDTO result = userUseCase.deleteUser(userId);
        assertNotNull(result);
        assertTrue(result.getSuccess());
        assertEquals("User deleted successfully", result.getMessage());
        verify(userRepository).delete(user);
    }

    @Test
    void deleteUserFailsWhenUserNotFound() {
        UUID userId =  UUID.fromString("c2c911d8-d79d-4285-b2c5-f0436ebdde72");
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> {
            userUseCase.deleteUser(userId);
        });
    }

    @Test
    void getAllUsersSuccessfully() {
        List<User> users = Arrays.asList(new User(), new User());
        when(userRepository.findAll()).thenReturn(users);
        when(modelMapper.map(any(User.class), eq(UserDTO.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            UserDTO userDTO = new UserDTO();
            userDTO.setToken(user.getToken());
            return userDTO;
        });
        List<UserDTO> result = userUseCase.getAllUsers();
        assertNotNull(result);
        assertEquals(users.size(), result.size());
    }

    @Test
    void getUserByIdSuccessfully() {
        UUID userId =  UUID.fromString("c2c911d8-d79d-4285-b2c5-f0436ebdde72");
        User user = new User();
        user.setId(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(modelMapper.map(any(User.class), eq(UserDTO.class))).thenReturn(new UserDTO());
        UserDTO result = userUseCase.getUserById(userId);
        assertNotNull(result);
    }

    @Test
    void getUserByIdFailsWhenUserNotFound() {
        UUID userId =  UUID.fromString("c2c911d8-d79d-4285-b2c5-f0436ebdde74");
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> {
            userUseCase.getUserById(userId);
        });
    }

}


