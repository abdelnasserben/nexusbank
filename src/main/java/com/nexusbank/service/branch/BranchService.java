package com.nexusbank.service.branch;

import com.nexusbank.dto.BranchDTO;
import com.nexusbank.mapper.BranchMapper;
import com.nexusbank.model.Branch;
import com.nexusbank.repository.BranchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BranchService {

    @Autowired
    BranchRepository repository;

    public BranchDTO save(BranchDTO branchDTO) {

        if (branchDTO == null) {
            System.err.println("Branch can't be null");
            return new BranchDTO();
        }

        return BranchMapper.INSTANCE.toDto(repository.save(BranchMapper.INSTANCE.toModel(branchDTO)));
    }

    public List<BranchDTO> findAll(String filter) {

        List<Branch> branches = filter == null || filter.isEmpty() ? repository.findAll() : repository.findAllByBranchNameContainingIgnoreCase(filter);
        return branches.stream()
                .map(BranchMapper.INSTANCE::toDto)
                .toList();
    }

    public void delete(BranchDTO branchDTO) {
        repository.delete(BranchMapper.INSTANCE.toModel(branchDTO));
    }
}
