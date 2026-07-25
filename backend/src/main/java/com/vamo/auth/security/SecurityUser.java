package com.vamo.auth.security;

import java.util.Collection;
import java.util.List;
import com.vamo.user.entity.User;
import jakarta.annotation.Nullable;
import lombok.NonNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public record SecurityUser(User user) implements UserDetails {
	
	@Override
	public @NonNull String getUsername() {
		return user.getPhoneNumber();
	}
	
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}
	
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}
	
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}
	
	@Override
	public boolean isEnabled() {
		return true;
	}
	
	@Override
	public @Nullable String getPassword() {
		return user.getPasswordHash();
	}
	
	@Override
	public @NonNull Collection<? extends GrantedAuthority> getAuthorities() {
		return List.of(new SimpleGrantedAuthority(user.getRole().name()));
	}
}
