package by.fk8ght.task.trip.client.fallback;

import by.fk8ght.task.trip.client.PassengerClient;
import by.fk8ght.task.trip.model.dto.PassengerDTO;

public class PassengerClientFallback implements PassengerClient {
    @Override
    public PassengerDTO getPassenger(long id) {
        return new PassengerDTO(
                id,
                "UNKNOWN",
                "UNKNOWN",
                "UNKNOWN"
        );
    }
}
