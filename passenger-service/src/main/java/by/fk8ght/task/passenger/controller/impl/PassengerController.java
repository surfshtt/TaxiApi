package by.fk8ght.task.passenger.controller.impl;

import by.fk8ght.task.passenger.controller.PassengerApi;
import by.fk8ght.task.passenger.model.dto.PassengerDTO;
import by.fk8ght.task.passenger.service.PassengerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class PassengerController implements PassengerApi {
    private final PassengerService passengerService;

    @Override
    public ResponseEntity<Page<PassengerDTO>> getPassengers(Pageable pageable) {
        Page<PassengerDTO> passengers = passengerService.getPassengers(pageable);
        return ResponseEntity.ok(passengers);
    }

    @Override
    public ResponseEntity<PassengerDTO> getPassenger(long id) {
        PassengerDTO passenger = passengerService.getPassenger(id);
        return ResponseEntity.ok(passenger);
    }

    @Override
    public ResponseEntity<Void> savePassenger(@Valid PassengerDTO passenger) {
        passengerService.save(passenger);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Override
    public ResponseEntity<Void> updatePassanger(long id, @Valid PassengerDTO passenger) {
        passengerService.update(id, passenger);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Override
    public ResponseEntity<Void> deletePassenger(long id) {
        passengerService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
