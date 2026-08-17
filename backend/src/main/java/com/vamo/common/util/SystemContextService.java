package com.vamo.common.util;

import com.vamo.user.entity.User;
import com.vamo.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public class SystemContextService {
	
	private final UserRepository userRepository;
	
	@Value("${ADMIN_PHONE_NUMBER}")
	String adminPhoneNumber;
	private UUID cachedAdminId;
	
	public SystemContextService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	public UUID getAdminId() {
		if (this.cachedAdminId == null) {
			User admin = userRepository.findByPhoneNumber(adminPhoneNumber)
					.orElseThrow(() -> new IllegalStateException("Admin user not seeded properly."));
			this.cachedAdminId = admin.getId();
		}
		return this.cachedAdminId;
	}
}
