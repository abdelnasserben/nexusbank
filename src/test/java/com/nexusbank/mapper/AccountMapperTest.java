package com.nexusbank.mapper;

import com.nexusbank.dto.AccountDTO;
import com.nexusbank.dto.BranchDTO;
import com.nexusbank.model.Account;
import com.nexusbank.model.Branch;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AccountMapperTest {

    @Test
    void shouldMapAccountToDTO() {
        //given
        Account account = new Account();
        account.setAccountName("John Doe");
        account.setAccountNumber("123456");

        //when
        AccountDTO accountDTO = AccountMapper.INSTANCE.toDto(account);

        //then
        assertEquals("John Doe", accountDTO.getAccountName());
        assertEquals("123456", accountDTO.getAccountNumber());
    }

    @Test
    void shouldMapAccountDtoToAccount() {
        //given
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setAccountName("John Doe");
        accountDTO.setAccountNumber("123456");
        accountDTO.setBranch(BranchDTO.builder()
                .branchName("Branch BE")
                .branchAddress("London")
                .build());

        //when
        Account account = AccountMapper.INSTANCE.toModel(accountDTO);
        Branch branch = account.getBranch();

        //then
        assertEquals("John Doe", account.getAccountName());
        assertEquals("123456", account.getAccountNumber());
        assertEquals("Branch BE", branch.getBranchName());
    }
}