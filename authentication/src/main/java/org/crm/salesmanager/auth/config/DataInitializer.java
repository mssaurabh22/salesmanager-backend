package org.crm.salesmanager.auth.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.crm.salesmanager.user.entity.Role;
import org.crm.salesmanager.user.entity.User;
import org.crm.salesmanager.user.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    
    @Override
    public void run(String... args) throws Exception {
        if (userRepository.count() == 0) {
            log.info("Initializing default users...");
            
            // Create admin user
            User admin = User.builder()
                    .name("Admin User")
                    .email("admin@crm.com")
                    .phone("+1234567890")
                    .password(passwordEncoder.encode("admin123"))
                    .role(Role.ADMIN)
                    .active(true)
                    .build();
            
            // Create employee user
            User employee = User.builder()
                    .name("Employee User")
                    .email("employee@crm.com")
                    .phone("+1234567891")
                    .password(passwordEncoder.encode("employee123"))
                    .role(Role.EMPLOYEE)
                    .active(true)
                    .build();
            
            userRepository.save(admin);
            userRepository.save(employee);
            
            log.info("Default users created successfully");
            log.info("Admin credentials: admin@crm.com / admin123");
            log.info("Employee credentials: employee@crm.com / employee123");
        }
    }
}