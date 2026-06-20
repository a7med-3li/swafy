package com.swafy.auth.security;

import com.swafy.user.entity.User;
import com.swafy.user.repository.UserRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class JpaUserDetailsService implements UserDetailsService {
	
	private final UserRepository userRepository;
	
	@Override
	public @NonNull UserDetails loadUserByUsername(@NonNull String phoneNumber) throws UsernameNotFoundException {
		User user = userRepository.findByPhoneNumber(phoneNumber)
				.orElseThrow(() -> new UsernameNotFoundException("User not found with phone number: " + phoneNumber));
		return new SecurityUser(user);
	}
}
