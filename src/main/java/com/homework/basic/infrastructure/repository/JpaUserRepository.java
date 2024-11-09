package com.homework.basic.infrastructure.repository;

import com.homework.basic.domain.entity.User;
import com.homework.basic.domain.repository.UserRepository;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface JpaUserRepository extends JpaRepository<User, Long>, UserRepository {

  @Query("SELECT u FROM p_users u JOIN FETCH u.roles WHERE u.username = :username")
  Optional<User> findByUsernameWithRoles(String username);
}
