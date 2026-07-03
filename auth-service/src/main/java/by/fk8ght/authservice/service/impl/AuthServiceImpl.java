package by.fk8ght.authservice.service.impl;

import by.fk8ght.authservice.model.dto.CredentialsRequestDTO;
import by.fk8ght.authservice.model.dto.LoginRequestDTO;
import by.fk8ght.authservice.model.dto.TokenRequestDTO;
import by.fk8ght.authservice.model.dto.TokenResponseDTO;
import by.fk8ght.authservice.model.entity.Role;
import by.fk8ght.authservice.model.entity.UserCredentialsEntity;
import by.fk8ght.authservice.repository.UserCredentialsRepository;
import by.fk8ght.authservice.exception.BadRequestException;
import by.fk8ght.authservice.exception.UnauthorizedException;
import by.fk8ght.authservice.service.AuthService;
import by.fk8ght.authservice.util.PasswordHasher;
import by.fk8ght.jwtsecurity.security.JwtService;
import by.fk8ght.jwtsecurity.security.JwtUserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserCredentialsRepository credentialsRepository;
    private final JwtService jwtService;
    private final PasswordHasher hasher;

    @Override
    public TokenResponseDTO login(LoginRequestDTO request) {
        UserCredentialsEntity credentials = credentialsRepository.findByLogin(request.getLogin())
                .orElseThrow(() -> new UnauthorizedException("Invalid login or password"));

        if (!hasher.verifyPassword(request.getPassword(), credentials.getPasswordHash())) {
            throw new UnauthorizedException("Invalid login or password");
        }

        return buildTokenResponse(credentials);
    }

    @Override
    public TokenResponseDTO refreshToken(TokenRequestDTO request) {
        JwtUserPrincipal principal;
        try {
            principal = jwtService.parseRefreshToken(request.getToken());
        } catch (Exception ex) {
            throw new UnauthorizedException("Invalid refresh token");
        }
        if (!credentialsRepository.existsByUserId(principal.getUserId())) {
            throw new UnauthorizedException("User wasn't found");
        }

        return buildTokenResponse(credentialsRepository.findByLogin(principal.getLogin())
                .orElseThrow(() -> new UnauthorizedException("Invalid login or password")));
    }

    @Override
    public void saveCredentials(CredentialsRequestDTO request) {
        if (credentialsRepository.existsByLogin(request.getLogin())) {
            throw new BadRequestException("Login already exists");
        }
        if (credentialsRepository.existsByUserId(request.getUserId())) {
            throw new BadRequestException("Credentials already exist for this user");
        }

        UserCredentialsEntity credentials = new UserCredentialsEntity();
        credentials.setUserId(request.getUserId());
        credentials.setLogin(request.getLogin());
        credentials.setPasswordHash(hasher.hashPassword(request.getPassword()));
        credentials.setRole(Role.USER);
        credentialsRepository.save(credentials);
    }

    private TokenResponseDTO buildTokenResponse(UserCredentialsEntity credentials) {
        String accessToken = jwtService.generateAccessToken(credentials.getUserId(), credentials.getRole().name(), credentials.getLogin());
        String refreshToken = jwtService.generateRefreshToken(credentials.getUserId(), credentials.getRole().name(), credentials.getLogin());
        return new TokenResponseDTO(accessToken, refreshToken);
    }
}
