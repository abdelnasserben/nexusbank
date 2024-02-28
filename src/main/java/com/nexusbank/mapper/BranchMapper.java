package com.nexusbank.mapper;

import com.nexusbank.dto.BranchDTO;
import com.nexusbank.model.Branch;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface BranchMapper {
    BranchMapper INSTANCE = Mappers.getMapper(BranchMapper.class);

    Branch toModel(BranchDTO branchDTO);

    BranchDTO toDto(Branch branch);
}
