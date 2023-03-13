package ru.kazov.collectivepurchases.server.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import ru.kazov.collectivepurchases.server.models.dao.User;

import java.security.Key;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class JwtService {

    private static final Logger logger = LoggerFactory.getLogger(JwtService.class);

    @Value("${app.jwt.issuer}")
    private String issuer;

    public String generateAccessToken(User user) {
        return Jwts.builder()
                .setSubject(user.getEmail())
                .setIssuer(issuer)
                .setIssuedAt(new Date())
                .setExpiration(new Date(Long.MAX_VALUE))
                .signWith(getSignKeys(user.getSecret()), SignatureAlgorithm.HS512)
                .compact();
    }

    public String extractUserEmail(String token) {
        final String unsignedToken = token.substring(0, token.lastIndexOf(".") + 1);

        return Jwts.parserBuilder()
                .build()
                .parseClaimsJwt(unsignedToken).getBody().getSubject();

    }

    public boolean validateToken(String token, UserDetails userDetails) {
        User user = (User) userDetails;
        String message;
        try {
            String userName = Jwts.parserBuilder()
                    .setSigningKey(getSignKeys(user.getSecret()))
                    .requireIssuer(issuer)
                    .build()
                    .parseClaimsJws(token).getBody().getSubject();

            return userName.equals(userDetails.getUsername());


        } catch (ExpiredJwtException ex) {
            message = "JWT expired";
        } catch (IllegalArgumentException ex) {
            message = "Token is null, empty or only whitespace";
        } catch (MalformedJwtException ex) {
            message = "JWT is invalid";
        } catch (UnsupportedJwtException ex) {
            message = "JWT is not supported";
        } catch (SecurityException ex) {
            message = "Signature validation failed";
        } catch (io.jsonwebtoken.security.SignatureException ex) {
            message = "JWT signature does not match locally computed signature";
        }
        return false;
    }

    private Key getSignKeys(String secret) {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
    }
}
