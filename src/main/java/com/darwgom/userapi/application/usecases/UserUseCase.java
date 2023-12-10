package com.darwgom.userapi.application.usecases;

import com.darwgom.userapi.application.dto.UserDTO;
import com.darwgom.userapi.application.dto.UserInputDTO;

public interface UserUseCase {
    UserDTO registerUser(UserInputDTO userInputDTO);
}
