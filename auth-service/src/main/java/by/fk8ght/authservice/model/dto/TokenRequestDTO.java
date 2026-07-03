package by.fk8ght.authservice.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class TokenRequestDTO {

    @NotBlank
    private String token;
}
