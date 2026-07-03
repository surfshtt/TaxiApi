package by.fk8ght.authservice.service;

import by.fk8ght.authservice.model.dto.CredentialsRequestDTO;
import by.fk8ght.authservice.model.dto.LoginRequestDTO;
import by.fk8ght.authservice.model.dto.TokenRequestDTO;
import by.fk8ght.authservice.model.dto.TokenResponseDTO;

public interface AuthService {

    TokenResponseDTO login(LoginRequestDTO request);

    TokenResponseDTO refreshToken(TokenRequestDTO request);

    void saveCredentials(CredentialsRequestDTO request);
}
