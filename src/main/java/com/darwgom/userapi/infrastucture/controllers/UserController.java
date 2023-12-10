package com.darwgom.userapi.infrastucture.controllers;

import com.darwgom.userapi.application.dto.UserDTO;
import com.darwgom.userapi.application.dto.UserInputDTO;
import com.darwgom.userapi.application.usecases.UserUseCase;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("api/users")
public class UserController {
    @Autowired
    private UserUseCase userUseCase;

    @PostMapping("/register")
    public ResponseEntity<UserDTO> registerUser(@RequestBody UserInputDTO userInputDTO) {
        UserDTO newUser = userUseCase.registerUser(userInputDTO);
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }

}
