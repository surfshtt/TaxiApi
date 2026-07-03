package by.fk8ght.task.car.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class CarDTO {
    private Long id;
    @NotBlank
    private String color;
    @NotBlank
    private String brand;
    @NotBlank
    @Pattern(regexp = "^\\d{4} [A-Z]{2}-\\d$", message = "Plate must match Belarus format")
    private String plateNumber;
    @NotNull
    private Long driverId;
}
