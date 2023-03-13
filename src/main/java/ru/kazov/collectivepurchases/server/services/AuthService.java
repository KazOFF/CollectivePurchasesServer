package ru.kazov.collectivepurchases.server.services;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kazov.collectivepurchases.server.models.dao.User;
import ru.kazov.collectivepurchases.server.models.dao.UserRole;
import ru.kazov.collectivepurchases.server.repositories.UserRepository;
import ru.kazov.collectivepurchases.server.security.JwtService;

import javax.crypto.SecretKey;

@RequiredArgsConstructor
@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Transactional
    public String registerNewUser(@NotBlank String email, @NotBlank String password) {
        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(UserRole.USER);
        user.setSecret(generateSecretString());
        userRepository.save(user);
        return jwtService.generateAccessToken(user);
    }

    @Transactional
    public String loginUser(@NotBlank String email, @NotBlank String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return jwtService.generateAccessToken(user);
    }

    @Transactional
    public void sendRestoreCode(@NotBlank String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        user.setRestoreCode(generateSecretString());
    }

    @Transactional
    public void restorePassword(@NotBlank String email, @NotBlank String restoreCode, @NotBlank String newPassword) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        if (user.getRestoreCode().equals(restoreCode)) {
            user.setPassword(passwordEncoder.encode(newPassword));
            user.setRestoreCode("");
            user.setSecret(generateSecretString());
        }
    }

    private String generateSecretString() {
        SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS512);
        return Encoders.BASE64.encode(key.getEncoded());
    }

}
