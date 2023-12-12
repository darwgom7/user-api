package com.darwgom.userapi.application.usecases;

import com.darwgom.userapi.application.dto.LoginDTO;
import com.darwgom.userapi.application.dto.TokenDTO;
import com.darwgom.userapi.application.dto.UserDTO;
import com.darwgom.userapi.application.dto.UserInputDTO;
import com.darwgom.userapi.application.dto.UserDeleteDTO;

import java.util.List;
import java.util.UUID;

public interface UserUseCase {
    UserDTO registerUser(UserInputDTO userInputDTO);
    TokenDTO loginUser(LoginDTO loginDTO);
    List<UserDTO> getAllUsers();
    UserDTO getUserById(UUID userId);
    UserDTO updateUser(UUID userId, UserInputDTO userInputDTO);
    UserDeleteDTO deleteUser(UUID userId);
}
