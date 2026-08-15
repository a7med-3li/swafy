package com.vamo.auth.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.UUID;

public class JwtAuthentication extends AbstractAuthenticationToken {

    private final String userId;
    private final UUID passengerId;

    public JwtAuthentication(String userId, Collection<? extends GrantedAuthority> authorities, UUID passengerId) {
        super(authorities);
        this.userId = userId;
        this.passengerId = passengerId;
        super.setAuthenticated(true); // Must use super, as we validated the token
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return this.userId;
    }

    public String getUserId() {
        return userId;
    }
    
    public UUID getPassengerId() {
        return this.passengerId;
    }
}

