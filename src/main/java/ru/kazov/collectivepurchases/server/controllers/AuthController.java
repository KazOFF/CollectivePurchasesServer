package ru.kazov.collectivepurchases.server.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.kazov.collectivepurchases.server.models.dto.ErrorResponse;
import ru.kazov.collectivepurchases.server.models.dto.auth.LoginRequest;
import ru.kazov.collectivepurchases.server.models.dto.auth.LoginResponse;
import ru.kazov.collectivepurchases.server.models.dto.auth.RegisterRequest;
import ru.kazov.collectivepurchases.server.models.dto.auth.RegisterResponse;
import ru.kazov.collectivepurchases.server.services.AuthService;

import java.net.URI;

@RequiredArgsConstructor
@RestController
@Tag(name = "Authentication", description = "Authentication API")
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "Register new user")
    @ApiResponse(responseCode = "201")
    @PostMapping(value = "register", consumes = "application/json", produces = "application/json")
    public ResponseEntity<RegisterResponse> register(@RequestBody RegisterRequest request) {

        String jwtToken = authService.registerNewUser(request.getEmail(), request.getPassword());
        return ResponseEntity.created(URI.create("/profile")).body(new RegisterResponse(jwtToken));
    }

    @Operation(summary = "Login")
    @ApiResponse(responseCode = "200")
    @PostMapping(value = "login", consumes = "application/json", produces = "application/json")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {

        String jwtToken = authService.loginUser(request.getEmail(), request.getPassword());
        return ResponseEntity.ok(new LoginResponse(jwtToken));
    }

    @Operation(summary = "Check token")
    @ApiResponse(responseCode = "204")
    @GetMapping(value = "check")
    public ResponseEntity<Void> check() {
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Check error")
    @ApiResponse(responseCode = "200")
    @GetMapping(value = "error")
    public ResponseEntity<ErrorResponse> checkError() {
        return ResponseEntity.ok(new ErrorResponse("error message", "requested url"));
    }
}
