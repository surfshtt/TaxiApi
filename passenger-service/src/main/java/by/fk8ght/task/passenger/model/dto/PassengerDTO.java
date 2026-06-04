package by.fk8ght.task.passenger.model.dto;

import jakarta.validation.constraints.*;
import lombok.Data;


@Data
public class PassengerDTO {
    private Long id;
    @NotBlank
    private String name;
    @NotBlank
    private String email;
    @NotBlank
    private String phone;
}
