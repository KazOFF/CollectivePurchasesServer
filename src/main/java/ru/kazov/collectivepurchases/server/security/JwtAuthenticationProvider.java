package ru.kazov.collectivepurchases.server.security;

import jakarta.validation.constraints.NotNull;
import lombok.Setter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.util.Assert;


public class JwtAuthenticationProvider implements AuthenticationProvider, InitializingBean, MessageSourceAware {

    @Setter
    private JwtService jwtService;
    @Setter
    private UserDetailsService userDetailsService;
    private MessageSourceAccessor messages = SpringSecurityMessageSource.getAccessor();


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        Assert.isInstanceOf(JwtAuthenticationToken.class, authentication,
                () -> this.messages.getMessage("JwtAuthenticationProvider.onlySupports",
                        "Only JwtAuthenticationToken is supported"));

        UserDetails userDetails;

        try {
            final String jwtToken = (String) authentication.getPrincipal();
            final String userEmail = jwtService.extractUserEmail(jwtToken);

            if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                userDetails = userDetailsService.loadUserByUsername(userEmail);

                if (jwtService.validateToken(jwtToken, userDetails)) {
                    JwtAuthenticationToken jwtAuthenticationToken = JwtAuthenticationToken.authenticated(userDetails, null, userDetails.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(jwtAuthenticationToken);
                    return jwtAuthenticationToken;
                }
            }
        } catch (RuntimeException ex) {
            return null;
        }
        return null;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (JwtAuthenticationToken.class.isAssignableFrom(authentication));
    }


    @Override
    public void afterPropertiesSet() {
    }

    @Override
    public void setMessageSource(MessageSource messageSource) {
        this.messages = new MessageSourceAccessor(messageSource);
    }

}
