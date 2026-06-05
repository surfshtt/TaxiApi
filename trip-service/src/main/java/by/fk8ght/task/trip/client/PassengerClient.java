package by.fk8ght.task.trip.client;

import by.fk8ght.task.trip.client.fallback.PassengerClientFallback;
import by.fk8ght.task.trip.model.dto.PassengerDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "passenger-service", fallback = PassengerClientFallback.class)
public interface PassengerClient {
    @GetMapping("api/passenger/{id}")
    PassengerDTO getPassenger(@PathVariable long id);
}
