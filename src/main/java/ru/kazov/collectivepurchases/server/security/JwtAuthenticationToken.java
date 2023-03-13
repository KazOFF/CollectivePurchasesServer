package ru.kazov.collectivepurchases.server.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class JwtAuthenticationToken extends AbstractAuthenticationToken {

    private final Object principal;

    private final Object credentials;

    /**
     * Constructor used for an authentication request. The
     * {@link org.springframework.security.core.Authentication#isAuthenticated()} will
     * return <code>false</code>.
     *
     * @param aPrincipal   The pre-authenticated principal
     * @param aCredentials The pre-authenticated credentials
     */
    public JwtAuthenticationToken(Object aPrincipal, Object aCredentials) {
        super(null);
        this.principal = aPrincipal;
        this.credentials = aCredentials;
    }

    /**
     * Constructor used for an authentication response. The
     * {@link org.springframework.security.core.Authentication#isAuthenticated()} will
     * return <code>true</code>.
     *
     * @param aPrincipal    The authenticated principal
     * @param anAuthorities The granted authorities
     */
    public JwtAuthenticationToken(Object aPrincipal, Object aCredentials,
                                  Collection<? extends GrantedAuthority> anAuthorities) {
        super(anAuthorities);
        this.principal = aPrincipal;
        this.credentials = aCredentials;
        setAuthenticated(true);
    }

    public static JwtAuthenticationToken unauthenticated(Object principal, Object credentials) {
        return new JwtAuthenticationToken(principal, credentials);
    }

    /**
     * This factory method can be safely used by any code that wishes to create an
     * authenticated <code>UsernamePasswordAuthenticationToken</code>.
     *
     * @return UsernamePasswordAuthenticationToken with true isAuthenticated() result
     * @since 5.7
     */
    public static JwtAuthenticationToken authenticated(Object principal, Object credentials,
                                                       Collection<? extends GrantedAuthority> authorities) {
        return new JwtAuthenticationToken(principal, credentials, authorities);
    }

    /**
     * Get the credentials
     */
    @Override
    public Object getCredentials() {
        return this.credentials;
    }

    /**
     * Get the principal
     */
    @Override
    public Object getPrincipal() {
        return this.principal;
    }
}
