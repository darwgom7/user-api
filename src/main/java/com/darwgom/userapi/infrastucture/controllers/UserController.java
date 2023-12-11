package com.darwgom.userapi.infrastucture.controllers;

import com.darwgom.userapi.application.dto.*;
import com.darwgom.userapi.application.usecases.UserUseCase;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = {"*"})
@RequestMapping("api/users")
public class UserController {
    @Autowired
    private UserUseCase userUseCase;

    @PostMapping("/register")
    public ResponseEntity<UserDTO> registerUser(@RequestBody UserInputDTO userInputDTO) {
        UserDTO newUser = userUseCase.registerUser(userInputDTO);
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<TokenDTO> loginUser(@RequestBody LoginDTO loginDTO) {
        TokenDTO tokenDTO = userUseCase.loginUser(loginDTO);
        return ResponseEntity.ok(tokenDTO);
    }

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> usersDTO = userUseCase.getAllUsers();
        return new ResponseEntity<>(usersDTO, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        UserDTO userDTO = userUseCase.getUserById(id);
        return ResponseEntity.ok(userDTO);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long userId, @RequestBody UserInputDTO userInputDTO) {
        UserDTO updatedUser = userUseCase.updateUser(userId, userInputDTO);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<UserDeleteDTO> deleteUser(@PathVariable Long id) {
        UserDeleteDTO response = userUseCase.deleteUser(id);
        return ResponseEntity.ok(response);
    }

}
