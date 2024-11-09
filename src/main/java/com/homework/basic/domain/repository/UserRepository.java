package com.homework.basic.domain.repository;

import com.homework.basic.domain.entity.User;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository {

  User save(User user);

  Optional<User> findById(Long id);

  Optional<User> findByUsername(String username);

  Optional<User> findByUsernameWithRoles(String username);

  void deleteAll();
}
