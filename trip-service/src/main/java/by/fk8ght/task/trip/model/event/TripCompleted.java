package by.fk8ght.task.trip.model.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TripCompleted {
    long tripId;
    long driverId;
    long passengerId;
}
