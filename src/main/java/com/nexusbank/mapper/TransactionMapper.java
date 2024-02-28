package com.nexusbank.mapper;

import com.nexusbank.dto.TransactionDTO;
import com.nexusbank.model.Transaction;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TransactionMapper {
    TransactionMapper INSTANCE = Mappers.getMapper(TransactionMapper.class);

    Transaction toModel(TransactionDTO transactionDTO);

    TransactionDTO toDto(Transaction transaction);
}
