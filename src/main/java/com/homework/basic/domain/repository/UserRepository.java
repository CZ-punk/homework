package com.homework.basic.domain.repository;

import com.homework.basic.domain.entity.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository {

    User save(User user);
    Optional<User> findById(Long id);
    Optional<User> findByUsername(String username);

}
