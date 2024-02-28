package com.nexusbank.service.customer;

import com.nexusbank.dto.CustomerDTO;
import com.nexusbank.mapper.CustomerMapper;
import com.nexusbank.model.Customer;
import com.nexusbank.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    @Autowired
    CustomerRepository repository;

    public CustomerDTO save(CustomerDTO customerDTO) {
        if (customerDTO == null) {
            System.err.println("Customer can't be null");
            return new CustomerDTO();
        }
        return CustomerMapper.INSTANCE.toDto(repository.save(CustomerMapper.INSTANCE.toModel(customerDTO)));
    }

    public List<CustomerDTO> findAll(String filter) {

        List<Customer> customers = filter == null || filter.isEmpty() ? repository.findAll() : repository.findAllByFirstNameContainingIgnoreCase(filter);
        return customers.stream()
                .map(CustomerMapper.INSTANCE::toDto)
                .toList();
    }

    public void delete(CustomerDTO customerDTO) {
        repository.delete(CustomerMapper.INSTANCE.toModel(customerDTO));
    }

    public CustomerDTO findByIdentity(String identityNumber) {
        return CustomerMapper.INSTANCE.toDto(repository.findByIdentityNumber(identityNumber));
    }

}
