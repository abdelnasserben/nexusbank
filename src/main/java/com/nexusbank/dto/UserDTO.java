package com.nexusbank.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Set;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class UserDTO extends BasicDTO {

    private String username;
    private String password;
    private boolean enabled;
    private Set<String> authorities;

}
