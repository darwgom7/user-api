package com.darwgom.userapi.domain.repositories;

import com.darwgom.userapi.domain.entities.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, String> {
}
