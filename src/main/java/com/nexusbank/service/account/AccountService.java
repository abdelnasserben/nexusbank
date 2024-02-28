package com.nexusbank.service.account;

import com.nexusbank.dto.AccountDTO;
import com.nexusbank.dto.BranchDTO;
import com.nexusbank.dto.TrunkDTO;
import com.nexusbank.mapper.AccountMapper;
import com.nexusbank.mapper.BranchMapper;
import com.nexusbank.mapper.TrunkMapper;
import com.nexusbank.model.Account;
import com.nexusbank.model.Trunk;
import com.nexusbank.repository.AccountRepository;
import com.nexusbank.repository.TrunkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountService {

    @Autowired
    AccountRepository repository;
    @Autowired
    TrunkRepository trunkRepository;

    public void save(AccountDTO accountDTO) {
        if (accountDTO == null) {
            System.err.println("Account can't be null");
            return;
        }
        repository.save(AccountMapper.INSTANCE.toModel(accountDTO));
    }

    public void save(TrunkDTO trunkDTO) {

        Account savedAccount = repository.save(AccountMapper.INSTANCE.toModel(trunkDTO.getAccount()));
        trunkDTO.setAccount(AccountMapper.INSTANCE.toDto(savedAccount));
        trunkRepository.save(TrunkMapper.INSTANCE.toModel(trunkDTO));
    }

    public List<AccountDTO> findAll(String filter) {

        List<Account> accounts = filter == null || filter.isEmpty() ? repository.findAll() : repository.findAllByAccountNameContainingIgnoreCase(filter);
        return accounts.stream()
                .map(AccountMapper.INSTANCE::toDto)
                .toList();
    }

    public void delete(AccountDTO accountDTO) {
        repository.delete(AccountMapper.INSTANCE.toModel(accountDTO));
    }

    public AccountDTO findByNumber(String accountNumber) {
        return AccountMapper.INSTANCE.toDto(repository.findByAccountNumber(accountNumber));
    }

    public TrunkDTO findTrunk(String accountNumber) {
        Account account = repository.findByAccountNumber(accountNumber);
        return TrunkMapper.INSTANCE.toDto(trunkRepository.findByAccount(account)
                .orElseGet(Trunk::new));
    }

    public AccountDTO findVault(BranchDTO branchDTO, String currency) {
        return AccountMapper.INSTANCE.toDto(repository.findByBranchAndCurrencyAndIsVault(BranchMapper.INSTANCE.toModel(branchDTO), currency, 1));
    }
}
