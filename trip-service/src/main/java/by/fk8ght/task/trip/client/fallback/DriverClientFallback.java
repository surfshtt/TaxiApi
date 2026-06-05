package by.fk8ght.task.trip.client.fallback;

import by.fk8ght.task.trip.client.DriverClient;
import by.fk8ght.task.trip.model.dto.DriverDTO;

public class DriverClientFallback implements DriverClient {
    @Override
    public DriverDTO getDriver(long id) {
        return new DriverDTO(
                id,
                "UNKNOWN",
                "UNKNOWN",
                "UNKNOWN"
        );
    }
}
