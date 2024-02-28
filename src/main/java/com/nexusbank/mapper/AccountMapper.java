package com.nexusbank.mapper;

import com.nexusbank.dto.AccountDTO;
import com.nexusbank.model.Account;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AccountMapper {
    AccountMapper INSTANCE = Mappers.getMapper(AccountMapper.class);

    Account toModel(AccountDTO accountDTO);

    AccountDTO toDto(Account account);
}
