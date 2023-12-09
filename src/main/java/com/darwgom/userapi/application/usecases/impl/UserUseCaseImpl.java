package com.darwgom.userapi.application.usecases.impl;

import com.darwgom.userapi.application.usecases.UserUseCase;
import com.darwgom.userapi.domain.repositories.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Service;

@Service
@EnableCaching
public class UserUseCaseImpl implements UserUseCase {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

}
