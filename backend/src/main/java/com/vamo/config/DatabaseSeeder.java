package com.vamo.config;

import java.time.Instant;
import com.vamo.common.enums.Gender;
import com.vamo.common.enums.UserRole;
import com.vamo.user.entity.User;
import com.vamo.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DatabaseSeeder {
	
	@Value("${ADMIN_PHONE_NUMBER}")
	String adminPhone;
	
	@Value("${ADMIN_PASSWORD}")
	String password;
	
	@Bean
	public CommandLineRunner seedDatabase(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		return args -> {
			
			if (userRepository.findByPhoneNumber(adminPhone).isEmpty()) {
				User admin = new User();
				admin.setPhoneNumber(adminPhone);
				admin.setFirstName("Ahmed");
				admin.setLastName("Ali");
				admin.setRole(UserRole.ADMIN);
				admin.setDeleted(false);
				admin.setCreatedAt(Instant.now());
				admin.setGender(Gender.MALE);
				admin.setPasswordHash(passwordEncoder.encode(password));
				userRepository.save(admin);
			} else {
				System.out.println("Admin user already exists. Skipping setup.");
			}
		};
	}
}
