package com.nexusbank.service;

import com.nexusbank.dto.AccountDTO;
import com.nexusbank.service.account.AccountService;
import com.nexusbank.service.branch.BranchService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class AccountServiceTest {

    @Autowired
    AccountService accountService;
    @Autowired
    BranchService branchService;


    @Test
    void shouldSaveAccount() {
        //given
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setAccountName("Sarah Hunt");
        accountDTO.setAccountNumber("123456");
        accountDTO.setBranch(branchService.findAll(null).get(0));

        //when
        accountService.save(accountDTO);
        AccountDTO savedAccount = accountService.findAll(null).get(0);

        //then
        assertNotNull(savedAccount.getAccountId());
        assertEquals("Sarah Hunt", savedAccount.getAccountName());
    }

}