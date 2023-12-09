package com.darwgom.userapi.infrastucture.controllers;

import com.darwgom.userapi.application.usecases.UserUseCase;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("user/api")
public class UserController {
    @Autowired
    private UserUseCase userUseCase;

}
