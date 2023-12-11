package com.darwgom.userapi.domain.repositories;

import com.darwgom.userapi.domain.entities.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findByEmail(String email);
    @NonNull
    @Override
    List<User> findAll();
}
