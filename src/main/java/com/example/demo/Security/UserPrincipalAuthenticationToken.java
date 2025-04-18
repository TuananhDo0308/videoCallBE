package com.example.demo.Security;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

public class UserPrincipalAuthenticationToken  extends AbstractAuthenticationToken {
    private final UserPrincipal principal;

    public UserPrincipalAuthenticationToken(UserPrincipal principal){
        super( principal.getAuthorities());
        this.principal=principal;
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public UserPrincipal getPrincipal() {
        return principal;
    }
}
