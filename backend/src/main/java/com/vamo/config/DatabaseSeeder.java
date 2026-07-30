package com.vamo.config;

import com.vamo.common.enums.UserRole;
import com.vamo.user.entity.User;
import com.vamo.user.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DatabaseSeeder {
	
	@Bean
	public CommandLineRunner seedDatabase(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		return args -> {
			String adminPhone = "01274392996";
			
			if (userRepository.findByPhoneNumber(adminPhone).isEmpty()) {
				User admin = new User();
				admin.setPhoneNumber(adminPhone);
				admin.setPasswordHash(passwordEncoder.encode("password"));
				admin.setRole(UserRole.ADMIN);
				
				userRepository.save(admin);
			} else {
				System.out.println("Admin user already exists. Skipping setup.");
			}
		};
	}
}
