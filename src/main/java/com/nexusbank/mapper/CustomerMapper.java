package com.nexusbank.mapper;

import com.nexusbank.dto.CustomerDTO;
import com.nexusbank.model.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CustomerMapper {
    CustomerMapper INSTANCE = Mappers.getMapper(CustomerMapper.class);

    Customer toModel(CustomerDTO customerDTO);

    CustomerDTO toDto(Customer customer);
}
