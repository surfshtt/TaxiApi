package by.fk8ght.task.trip.controller.impl;

import by.fk8ght.task.trip.controller.TripApi;
import by.fk8ght.task.trip.model.dto.TripDTO;
import by.fk8ght.task.trip.model.dto.TripStatusDTO;
import by.fk8ght.task.trip.service.TripService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class TripController implements TripApi {
    private final TripService tripService;

    @Override
    public ResponseEntity<Page<TripDTO>> getTrips(Pageable pageable) {
        return ResponseEntity.ok(tripService.getTrips(pageable));
    }

    @Override
    public ResponseEntity<TripDTO> getTrip(long id) {
        return ResponseEntity.ok(tripService.getTrip(id));
    }

    @Override
    public ResponseEntity<Void> saveTrip(@Valid TripDTO trip) {
        tripService.save(trip);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Override
    public ResponseEntity<Void> updateTrip(long id, @Valid TripDTO trip) {
        tripService.update(id, trip);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Override
    public ResponseEntity<Void> deleteTrip(long id) {
        tripService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Override
    public ResponseEntity<Void> updateTripStatus(long id, @Valid TripStatusDTO status) {
        tripService.updateStatus(id, status.getStatus());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
