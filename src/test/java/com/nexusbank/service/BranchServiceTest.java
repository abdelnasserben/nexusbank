package com.nexusbank.service;

import com.nexusbank.dto.BranchDTO;
import com.nexusbank.service.branch.BranchService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class BranchServiceTest {

    @Autowired
    BranchService branchService;

    @Test
    void shouldSaveBranch() {
        //given
        BranchDTO branchDTO = BranchDTO.builder()
                .branchName("HQ")
                .branchAddress("Moroni")
                .build();

        //when
        branchService.save(branchDTO);
        BranchDTO savedBranch = branchService.findAll("HQ").get(0);

        //then
        assertNotNull(savedBranch.getBranchId());
        assertEquals("HQ", savedBranch.getBranchName());
    }
}