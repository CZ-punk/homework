package com.homework.basic.domain.repository;

import com.homework.basic.domain.entity.User;

import java.util.Optional;

public interface UserRepository {

    void save();

    Optional<User> findById(Long id);
    Optional<User> findByUsername(String username);

}
