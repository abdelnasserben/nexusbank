package com.nexusbank.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Data
@SuperBuilder
@NoArgsConstructor
public class BasicDTO {

    private String status;
    private BranchDTO branch;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}