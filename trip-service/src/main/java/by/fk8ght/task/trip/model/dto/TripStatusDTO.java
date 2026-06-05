package by.fk8ght.task.trip.model.dto;

import by.fk8ght.task.trip.model.entity.TripStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TripStatusDTO {
    @NotNull
    private TripStatus status;
}
