package com.homework.basic.infrastructure.repository;

import com.homework.basic.domain.entity.User;
import com.homework.basic.domain.repository.UserRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaUserRepository extends JpaRepository<User, Long>, UserRepository {

}
