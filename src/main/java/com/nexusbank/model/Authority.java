package com.nexusbank.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "authorities")
public class Authority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int authorityId;
    @ManyToOne
    @JoinColumn(name = "username")
    private User username;
    private String authority;
}
