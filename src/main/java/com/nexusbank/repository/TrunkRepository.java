package com.nexusbank.repository;

import com.nexusbank.model.Account;
import com.nexusbank.model.Trunk;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TrunkRepository extends JpaRepository<Trunk, Long> {
    Optional<Trunk> findByAccount(Account account);
}
