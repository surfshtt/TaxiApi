package by.fk8ght.task.trip.controller;

import by.fk8ght.task.trip.model.dto.TripDTO;
import by.fk8ght.task.trip.model.dto.TripStatusDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("api/trip")
public interface TripApi {
    @GetMapping
    ResponseEntity<Page<TripDTO>> getTrips(@PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable);

    @GetMapping("/{id}")
    ResponseEntity<TripDTO> getTrip(@PathVariable long id);

    @PutMapping
    ResponseEntity<Void> saveTrip(@RequestBody TripDTO trip);

    @PutMapping("/{id}")
    ResponseEntity<Void> updateTrip(@PathVariable long id, @RequestBody TripDTO trip);

    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteTrip(@PathVariable long id);

    @PatchMapping("/{id}/status")
    ResponseEntity<Void> updateTripStatus(@PathVariable long id, @RequestBody TripStatusDTO status);
}
