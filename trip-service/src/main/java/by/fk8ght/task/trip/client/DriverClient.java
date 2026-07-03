package by.fk8ght.task.trip.client;

import by.fk8ght.task.trip.client.fallback.DriverClientFallback;
import by.fk8ght.task.trip.model.dto.DriverDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "driver-service", fallback = DriverClientFallback.class)
public interface DriverClient {
    @GetMapping("api/driver/{id}")
    DriverDTO getDriver(@PathVariable long id);
}
