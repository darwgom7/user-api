package com.darwgom.userapi.infrastructure.controllers;

import com.darwgom.userapi.application.dto.UserDTO;
import com.darwgom.userapi.application.dto.UserInputDTO;
import com.darwgom.userapi.application.usecases.UserUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.darwgom.userapi.application.dto.LoginDTO;
import com.darwgom.userapi.application.dto.TokenDTO;
import com.darwgom.userapi.application.dto.UserDeleteDTO;

import org.springframework.http.MediaType;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.BDDMockito.*;

import java.util.List;
import java.util.UUID;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserUseCase userUseCase;

    @Test
    @WithMockUser
    void registerUserShouldReturnCreatedUser() throws Exception {
        UserInputDTO userInputDTO = new UserInputDTO();
        UserDTO userDTO = new UserDTO();

        when(userUseCase.registerUser(any(UserInputDTO.class))).thenReturn(userDTO);

        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(userInputDTO)))
                .andExpect(status().isCreated())
                .andExpect(content().json(new ObjectMapper().writeValueAsString(userDTO)));
    }

    @Test
    void loginUserShouldReturnToken() throws Exception {
        LoginDTO loginDTO = new LoginDTO();
        TokenDTO tokenDTO = new TokenDTO();

        when(userUseCase.loginUser(any(LoginDTO.class))).thenReturn(tokenDTO);

        mockMvc.perform(post("/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(loginDTO)))
                .andExpect(status().isOk())
                .andExpect(content().json(new ObjectMapper().writeValueAsString(tokenDTO)));
    }

    @Test
    @WithMockUser
    void getAllUsersShouldReturnListOfUsers() throws Exception {
        List<UserDTO> usersDTO = List.of(new UserDTO());

        when(userUseCase.getAllUsers()).thenReturn(usersDTO);

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(content().json(new ObjectMapper().writeValueAsString(usersDTO)));
    }

    @Test
    @WithMockUser
    void getUserByIdShouldReturnUser() throws Exception {
        UUID userId =  UUID.fromString("c2c911d8-d79d-4285-b2c5-f0436ebdde72");
        UserDTO userDTO = new UserDTO();

        when(userUseCase.getUserById(userId)).thenReturn(userDTO);

        mockMvc.perform(get("/api/users/" + userId))
                .andExpect(status().isOk())
                .andExpect(content().json(new ObjectMapper().writeValueAsString(userDTO)));
    }

    @Test
    @WithMockUser(authorities = "ROLE_ADMIN")
    void updateUserShouldReturnUpdatedUser() throws Exception {
        UUID userId =  UUID.fromString("c2c911d8-d79d-4285-b2c5-f0436ebdde72");
        UserInputDTO userInputDTO = new UserInputDTO();
        UserDTO updatedUserDTO = new UserDTO();

        when(userUseCase.updateUser(eq(userId), any(UserInputDTO.class))).thenReturn(updatedUserDTO);

        mockMvc.perform(put("/api/users/" + userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(userInputDTO)))
                .andExpect(status().isOk())
                .andExpect(content().json(new ObjectMapper().writeValueAsString(updatedUserDTO)));
    }

    @Test
    @WithMockUser(authorities = "ROLE_ADMIN")
    void deleteUserShouldReturnSuccessMessage() throws Exception {
        UUID userId =  UUID.fromString("c2c911d8-d79d-4285-b2c5-f0436ebdde72");
        UserDeleteDTO response = new UserDeleteDTO(true, "User deleted successfully");

        when(userUseCase.deleteUser(userId)).thenReturn(response);

        mockMvc.perform(delete("/api/users/" + userId))
                .andExpect(status().isOk())
                .andExpect(content().json(new ObjectMapper().writeValueAsString(response)));
    }

}
