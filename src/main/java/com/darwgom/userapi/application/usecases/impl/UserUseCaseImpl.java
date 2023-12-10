package com.darwgom.userapi.application.usecases.impl;

import com.darwgom.userapi.application.dto.UserDTO;
import com.darwgom.userapi.application.dto.UserInputDTO;
import com.darwgom.userapi.application.usecases.UserUseCase;
import com.darwgom.userapi.domain.entities.Phone;
import com.darwgom.userapi.domain.entities.Role;
import com.darwgom.userapi.domain.entities.User;
import com.darwgom.userapi.domain.enums.RoleName;
import com.darwgom.userapi.domain.repositories.RoleRepository;
import com.darwgom.userapi.domain.repositories.UserRepository;
import com.darwgom.userapi.infrastucture.security.JwtTokenProvider;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.EnableCaching;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Set;
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

    @Override
    public UserDTO registerUser(UserInputDTO userInputDTO) {

        User user = modelMapper.map(userInputDTO, User.class);
        user.setPassword(passwordEncoder.encode(userInputDTO.getPassword()));

        user.setActive(true);
        user.setLastLogin(LocalDateTime.now());

        Set<Phone> phoneEntities = userInputDTO.getPhones().stream()
                .map(phoneDTO -> modelMapper.map(phoneDTO, Phone.class))
                .collect(Collectors.toSet());

        phoneEntities.forEach(phone -> phone.setUser(user));
        user.setPhones(phoneEntities);
        user.getPhones().forEach(phone -> phone.setUser(user));

        Role defaultRole = roleRepository.findByName(RoleName.ROLE_USER)
                .orElseThrow(() -> new IllegalStateException("Default role not found."));
        user.setRoles(Set.of(defaultRole));

        String token = jwtTokenProvider.createToken(user.getEmail(), user.getRoles());
        String tokenId = jwtTokenProvider.getTokenId(token);
        user.setTokenIdentifier(tokenId);

        User savedUser = userRepository.save(user);

        UserDTO resultUserDTO = modelMapper.map(savedUser, UserDTO.class);
        resultUserDTO.setToken(token);

        return resultUserDTO;
    }

}
