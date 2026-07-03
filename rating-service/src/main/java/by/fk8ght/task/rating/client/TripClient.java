package by.fk8ght.task.rating.client;

import by.fk8ght.task.rating.model.dto.TripDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@FeignClient(name = "trip-service")
public interface TripClient {
    @GetMapping("/api/trip/{id}")
    TripDTO getTrip(@PathVariable long id);
}
