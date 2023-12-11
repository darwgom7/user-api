package com.darwgom.userapi.application.usecases.impl;

import com.darwgom.userapi.application.dto.*;
import com.darwgom.userapi.application.dto.UserDeleteDTO;
import com.darwgom.userapi.application.usecases.UserUseCase;
import com.darwgom.userapi.domain.entities.Phone;
import com.darwgom.userapi.domain.entities.Role;
import com.darwgom.userapi.domain.entities.User;
import com.darwgom.userapi.domain.enums.RoleNameEnum;
import com.darwgom.userapi.domain.repositories.RoleRepository;
import com.darwgom.userapi.domain.repositories.UserRepository;
import com.darwgom.userapi.infrastucture.security.JwtTokenProvider;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.EnableCaching;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@EnableCaching
public class UserUseCaseImpl implements UserUseCase {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public UserDTO registerUser(UserInputDTO userInputDTO) {

        User user = modelMapper.map(userInputDTO, User.class);
        user.setPassword(passwordEncoder.encode(userInputDTO.getPassword()));

        user.setIsActive(Boolean.TRUE);
        user.setLastLogin(LocalDateTime.now());

        Set<Phone> phoneEntities = userInputDTO.getPhones().stream()
                .map(phoneDTO -> modelMapper.map(phoneDTO, Phone.class))
                .collect(Collectors.toSet());

        phoneEntities.forEach(phone -> phone.setUser(user));
        user.setPhones(phoneEntities);
        user.getPhones().forEach(phone -> phone.setUser(user));

        RoleNameEnum roleNameEnum = RoleNameEnum.valueOf(userInputDTO.getRole().toUpperCase());
        Role role = roleRepository.findByName(roleNameEnum)
                .orElseThrow(() -> new IllegalStateException("Role not found: " + userInputDTO.getRole()));
        user.setRole(role);

        System.out.println("::::Role:::: " + user.getRole().getName().name());
        GrantedAuthority authority = new SimpleGrantedAuthority(user.getRole().getName().name());

        String token = jwtTokenProvider.createToken(user.getEmail(), Collections.singletonList(authority));

        user.setToken(token);

        User savedUser = userRepository.save(user);

        UserDTO resultUserDTO = modelMapper.map(savedUser, UserDTO.class);
        resultUserDTO.setToken(token);

        return resultUserDTO;
    }

    @Override
    public TokenDTO loginUser(LoginDTO loginDTO) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDTO.getEmail(),
                        loginDTO.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtTokenProvider.createToken(authentication.getName(), authentication.getAuthorities());

        User user = userRepository.findByEmail(loginDTO.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + loginDTO.getEmail()));

        GrantedAuthority authority = new SimpleGrantedAuthority(user.getRole().getName().name());

        String token;
        if (user.getToken() == null || jwtTokenProvider.isTokenExpired(user.getToken())) {
            token = jwtTokenProvider.createToken(authentication.getName(), Collections.singletonList(authority));
            user.setToken(token);
            userRepository.save(user);
        } else {
            token = user.getToken();
        }
        user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);
        TokenDTO tokenDTO = new TokenDTO();
        tokenDTO.setToken(token);
        return tokenDTO;
    }

    @Override
    public List<UserDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream().map(user -> {
            UserDTO userDTO = modelMapper.map(user, UserDTO.class);
            userDTO.setToken(user.getToken());
            return userDTO;
        }).collect(Collectors.toList());
    }

    @Override
    public UserDTO getUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));
        return modelMapper.map(user, UserDTO.class);
    }

    @Override
    public UserDTO updateUser(Long userId, UserInputDTO userInputDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));

        if (userInputDTO.getRole() != null) {
            RoleNameEnum roleNameEnum = RoleNameEnum.valueOf(userInputDTO.getRole().toUpperCase());
            Role role = roleRepository.findByName(roleNameEnum)
                    .orElseThrow(() -> new IllegalStateException("Role not found: " + userInputDTO.getRole()));
            user.setRole(role);
        }
        updatePhones(user, userInputDTO.getPhones());

        User updatedUser = userRepository.save(user);
        return modelMapper.map(updatedUser, UserDTO.class);
    }

    private void updatePhones(User user, Set<PhoneInputDTO> phoneDTOs) {
        Map<String, Phone> existingPhones = user.getPhones().stream()
                .collect(Collectors.toMap(Phone::getNumber, phone -> phone));

        for (PhoneInputDTO phoneDTO : phoneDTOs) {
            Phone phone = existingPhones.get(phoneDTO.getNumber());

            if (phone == null) {
                phone = modelMapper.map(phoneDTO, Phone.class);
                phone.setUser(user);
                user.getPhones().add(phone);
            } else {
                modelMapper.map(phoneDTO, phone);
            }
        }
    }

    @Override
    public UserDeleteDTO deleteUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));
        userRepository.delete(user);
        return new UserDeleteDTO(true, "User deleted successfully");
    }

}
