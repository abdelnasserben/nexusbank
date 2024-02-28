package com.nexusbank.repository;

import com.nexusbank.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    List<Customer> findAllByFirstNameContainingIgnoreCase(String filter);

    Customer findByIdentityNumber(String identityNumber);
}
