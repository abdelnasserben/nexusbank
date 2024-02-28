package com.nexusbank.service.branch;

import com.nexusbank.app.Generator;
import com.nexusbank.constant.AccountProfile;
import com.nexusbank.constant.AccountType;
import com.nexusbank.constant.Currency;
import com.nexusbank.dto.AccountDTO;
import com.nexusbank.dto.BranchDTO;
import com.nexusbank.service.account.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BranchFacadeService {

    @Autowired
    BranchService branchService;
    @Autowired
    AccountService accountService;

    public void create(BranchDTO branchDTO) {

        if (branchDTO.getBranchId() == null) {

            BranchDTO savedBranch = branchService.save(branchDTO);

            //TODO: build and save vault eur
            accountService.save(AccountDTO.builder()
                    .accountName(String.format("Vault EUR %d", savedBranch.getBranchId()))
                    .accountNumber(Generator.generateAccountNumber())
                    .accountType(AccountType.BUSINESS.name())
                    .accountProfile(AccountProfile.PERSONAL.name())
                    .currency(Currency.EUR.name())
                    .isVault(1)
                    .branch(savedBranch)
                    .status(savedBranch.getStatus())
                    .build());

            //TODO: build and save vault usd
            accountService.save(AccountDTO.builder()
                    .accountName(String.format("Vault USD %d", savedBranch.getBranchId()))
                    .accountNumber(Generator.generateAccountNumber())
                    .accountType(AccountType.BUSINESS.name())
                    .accountProfile(AccountProfile.PERSONAL.name())
                    .currency(Currency.USD.name())
                    .isVault(1)
                    .branch(savedBranch)
                    .status(savedBranch.getStatus())
                    .build());

            //TODO: build and save vault kmf
            accountService.save(AccountDTO.builder()
                    .accountName(String.format("Vault KMF %d", savedBranch.getBranchId()))
                    .accountNumber(Generator.generateAccountNumber())
                    .accountType(AccountType.BUSINESS.name())
                    .accountProfile(AccountProfile.PERSONAL.name())
                    .currency(Currency.KMF.name())
                    .isVault(1)
                    .branch(savedBranch)
                    .status(savedBranch.getStatus())
                    .build());

        } else branchService.save(branchDTO);
    }

    public List<BranchDTO> findAll() {
        return branchService.findAll(null);
    }
}
