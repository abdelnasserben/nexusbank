package com.nexusbank.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Data
@SuperBuilder
@NoArgsConstructor
public class BranchDTO {

    private Long branchId;
    @NotEmpty
    private String branchName;
    private String branchAddress;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
