package com.nexusbank.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CurrentTimestamp;
import org.hibernate.generator.EventType;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "branches")
public class Branch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long branchId;
    private String branchName;
    private String branchAddress;
    private String status;
    @CurrentTimestamp(event = EventType.INSERT)
    private LocalDateTime createdAt;
    @CurrentTimestamp(event = EventType.UPDATE)
    private LocalDateTime updatedAt;
}
