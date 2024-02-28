package com.nexusbank.repository;

import com.nexusbank.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    List<User> findAllByUsernameContainingIgnoreCase(String filter);

    Optional<User> findByUsername(String username);

    void deleteByUsername(String username);
}
