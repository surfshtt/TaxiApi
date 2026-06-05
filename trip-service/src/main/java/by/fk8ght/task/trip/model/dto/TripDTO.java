package by.fk8ght.task.trip.model.dto;

import by.fk8ght.task.trip.model.entity.TripStatus;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class TripDTO {
    private Long id;
    @NotNull
    private Long driverId;
    @NotNull
    private Long passengerId;
    @NotBlank
    private String pickupAddress;
    @NotBlank
    private String destinationAddress;
    private TripStatus status;
    private LocalDateTime orderedAt;
    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal cost;
}
