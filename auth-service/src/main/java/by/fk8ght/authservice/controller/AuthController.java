package by.fk8ght.authservice.controller;

import by.fk8ght.authservice.model.dto.CredentialsRequestDTO;
import by.fk8ght.authservice.model.dto.LoginRequestDTO;
import by.fk8ght.authservice.model.dto.TokenRequestDTO;
import by.fk8ght.authservice.model.dto.TokenResponseDTO;
import by.fk8ght.authservice.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<TokenResponseDTO> login(@Valid @RequestBody LoginRequestDTO request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/token/refresh")
    public ResponseEntity<TokenResponseDTO> refreshToken(@Valid @RequestBody TokenRequestDTO request) {
        return ResponseEntity.ok(authService.refreshToken(request));
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(@Valid @RequestBody CredentialsRequestDTO request) {
        authService.saveCredentials(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/credentials")
    public ResponseEntity<Void> saveCredentials(@Valid @RequestBody CredentialsRequestDTO request) {
        authService.saveCredentials(request);
        return ResponseEntity.ok().build();
    }
}
