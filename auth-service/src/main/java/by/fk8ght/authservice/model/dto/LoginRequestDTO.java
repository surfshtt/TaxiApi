package by.fk8ght.authservice.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class LoginRequestDTO {

    @NotBlank
    private String login;

    @NotBlank
    private String password;
}
