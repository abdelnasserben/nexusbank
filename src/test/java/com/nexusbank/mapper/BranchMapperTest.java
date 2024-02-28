package com.nexusbank.mapper;

import com.nexusbank.dto.BranchDTO;
import com.nexusbank.model.Branch;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BranchMapperTest {

    @Test
    void shouldMapBranchToDto() {
        //given
        Branch branch = new Branch();
        branch.setBranchName("HQ");
        branch.setBranchAddress("London");

        //when
        BranchDTO branchDTO = BranchMapper.INSTANCE.toDto(branch);

        //then
        assertEquals("HQ", branchDTO.getBranchName());
        assertEquals("London", branchDTO.getBranchAddress());
    }

    @Test
    void shouldMapBranchDtoToBranch() {
        //given
        BranchDTO branchDTO = new BranchDTO();
        branchDTO.setBranchName("HQ");
        branchDTO.setBranchAddress("London");

        //when
        Branch branch = BranchMapper.INSTANCE.toModel(branchDTO);

        //then
        assertEquals("HQ", branch.getBranchName());
        assertEquals("London", branch.getBranchAddress());
    }
}