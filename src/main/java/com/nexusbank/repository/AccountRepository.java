package com.nexusbank.repository;

import com.nexusbank.model.Account;
import com.nexusbank.model.Branch;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountRepository extends JpaRepository<Account, Long> {
    List<Account> findAllByAccountNameContainingIgnoreCase(String value);

    Account findByAccountNumber(String accountNumber);

    Account findByBranchAndCurrencyAndIsVault(Branch branch, String currency, int isVault);
}
