package com.nexusbank.service.security;

import com.nexusbank.dto.UserDTO;
import com.nexusbank.mapper.BranchMapper;
import com.nexusbank.mapper.UserMapper;
import com.nexusbank.model.Authority;
import com.nexusbank.repository.AuthorityRepository;
import com.nexusbank.repository.UserRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    UserRepository userRepository;
    AuthorityRepository authorityRepository;
    UserDetailsManager userDetailsManager;

    public UserService(UserRepository userRepository, AuthorityRepository authorityRepository, UserDetailsManager userDetailsManager) {
        this.userRepository = userRepository;
        this.authorityRepository = authorityRepository;
        this.userDetailsManager = userDetailsManager;
    }

    public void create(UserDTO userDTO) {

        if (!userDetailsManager.userExists(userDTO.getUsername())) {
            userDetailsManager.createUser(User.builder()
                    .username(userDTO.getUsername())
                    .password(PasswordEncoderFactories.createDelegatingPasswordEncoder().encode("123"))
                    .roles(userDTO.getAuthorities().toArray(String[]::new))
                    .build());
            com.nexusbank.model.User lastUserInserted = userRepository.findByUsername(userDTO.getUsername()).orElse(null);

            if (lastUserInserted != null) {
                lastUserInserted.setStatus(userDTO.getStatus());
                lastUserInserted.setBranch(BranchMapper.INSTANCE.toModel(userDTO.getBranch()));
                userRepository.save(lastUserInserted);
            }
        }
    }

    public List<UserDTO> findAll(String filter) {

        List<com.nexusbank.model.User> users = filter == null || filter.isEmpty() ? userRepository.findAll() : userRepository.findAllByUsernameContainingIgnoreCase(filter);
        return users.stream()
                .peek(user -> user.setAuthorities(authorityRepository.findAllByUsername(user).stream()
                        .map(Authority::getAuthority)
                        .collect(Collectors.toSet())))
                .map(UserMapper.INSTANCE::toDto)
                .toList();
    }

    public void delete(UserDTO userDTO) {
        userDetailsManager.deleteUser(userDTO.getUsername());
    }
}
