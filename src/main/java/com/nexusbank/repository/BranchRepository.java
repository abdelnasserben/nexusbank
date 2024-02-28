package com.nexusbank.repository;

import com.nexusbank.model.Branch;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BranchRepository extends JpaRepository<Branch, Long> {
    List<Branch> findAllByBranchNameContainingIgnoreCase(String value);
}
