package by.fk8ght.authservice.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class CredentialsRequestDTO {

    @NotNull
    private Long userId;

    @NotBlank
    private String login;

    @NotBlank
    private String password;
}
