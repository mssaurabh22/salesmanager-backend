package org.crm.salesmanager.user.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.crm.salesmanager.user.dto.UserAuthDTO;
import org.crm.salesmanager.user.entity.User;
import org.crm.salesmanager.user.repository.UserRepository;
import org.crm.salesmanager.user.service.UserService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    
    private final UserRepository userRepository;
    
    @Override
    public UserAuthDTO getUserForAuth(String email) {
        log.debug("Getting user for authentication with email: {}", email);
        
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
        
        return mapToUserAuthDTO(user);
    }
    
    @Override
    public UserAuthDTO getUserForAuthById(Long id) {
        log.debug("Getting user for authentication with id: {}", id);
        
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        
        return mapToUserAuthDTO(user);
    }
    
    private UserAuthDTO mapToUserAuthDTO(User user) {
        return UserAuthDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .password(user.getPassword())
                .role(user.getRole())
                .active(user.getActive())
                .build();
    }
}