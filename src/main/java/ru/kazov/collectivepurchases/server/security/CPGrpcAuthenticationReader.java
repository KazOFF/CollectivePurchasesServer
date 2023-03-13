package ru.kazov.collectivepurchases.server.security;

import io.grpc.Metadata;
import io.grpc.ServerCall;
import jakarta.annotation.Nullable;
import net.devh.boot.grpc.server.security.authentication.GrpcAuthenticationReader;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import static net.devh.boot.grpc.common.security.SecurityConstants.AUTHORIZATION_HEADER;


public class CPGrpcAuthenticationReader implements GrpcAuthenticationReader {


    @Nullable
    @Override
    public Authentication readAuthentication(ServerCall<?, ?> serverCall, Metadata metadata) throws AuthenticationException {
        final String header = metadata.get(AUTHORIZATION_HEADER);
        if (header == null || !header.toLowerCase().startsWith("bearer ")) {
            throw new AuthenticationCredentialsNotFoundException("Token not provided");
        }
        final String accessToken = header.substring(7);

        return JwtAuthenticationToken.unauthenticated(accessToken, null);
    }
}