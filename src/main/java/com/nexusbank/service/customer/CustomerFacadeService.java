package com.nexusbank.service.customer;

import com.nexusbank.app.Generator;
import com.nexusbank.constant.AccountProfile;
import com.nexusbank.constant.AccountType;
import com.nexusbank.constant.Currency;
import com.nexusbank.dto.AccountDTO;
import com.nexusbank.dto.CustomerDTO;
import com.nexusbank.dto.TrunkDTO;
import com.nexusbank.service.account.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerFacadeService {

    @Autowired
    CustomerService customerService;
    @Autowired
    AccountService accountService;

    public void create(CustomerDTO customerDTO) {

        if (customerDTO.getCustomerId() == null) {
            CustomerDTO savedCustomer = customerService.save(customerDTO);

            //TODO: create trunk kmf
            accountService.save(TrunkDTO.builder()
                    .customer(savedCustomer)
                    .account(AccountDTO.builder()
                            .accountName(String.format("%s %s", savedCustomer.getFirstName(), savedCustomer.getLastName()))
                            .accountNumber(Generator.generateAccountNumber())
                            .accountType(AccountType.CURRENT.name())
                            .accountProfile(AccountProfile.PERSONAL.name())
                            .currency(Currency.KMF.name())
                            .branch(savedCustomer.getBranch())
                            .status(savedCustomer.getStatus())
                            .build())
                    .build());

        } else customerService.save(customerDTO);
    }
}
