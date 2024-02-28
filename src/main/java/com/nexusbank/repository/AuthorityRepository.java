package com.nexusbank.repository;

import com.nexusbank.model.Authority;
import com.nexusbank.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuthorityRepository extends JpaRepository<Authority, String> {
    List<Authority> findAllByUsername(User user);
}
