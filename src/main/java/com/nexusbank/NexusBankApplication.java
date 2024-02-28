package com.nexusbank;

import com.nexusbank.constant.Status;
import com.nexusbank.dto.BranchDTO;
import com.nexusbank.dto.UserDTO;
import com.nexusbank.service.branch.BranchService;
import com.nexusbank.service.security.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Set;

@SpringBootApplication
public class NexusBankApplication {

    public static void main(String[] args) {
        SpringApplication.run(NexusBankApplication.class, args);
    }

    @Bean
    CommandLineRunner loadData(BranchService branchService, UserService userService) {
        return args -> {
            BranchDTO branchDTO = branchService.save(BranchDTO.builder()
                    .branchName("HQ")
                    .branchAddress("London")
                    .build());

            userService.create(UserDTO.builder()
                    .username("admin")
                    .password("123")
                    .authorities(Set.of("USER", "ADMIN"))
                    .status(Status.ACTIVE.name())
                    .branch(branchDTO)
                    .build());
        };
    }
}
