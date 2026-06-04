package by.fk8ght.task.passenger.controller;

import by.fk8ght.task.passenger.model.dto.PassengerDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("api/passenger")
public interface PassengerApi {
    @GetMapping
    ResponseEntity<Page<PassengerDTO>> getPassengers(@PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable);

    @GetMapping("/{id}")
    ResponseEntity<PassengerDTO> getPassenger(@PathVariable long id);

    @PutMapping
    ResponseEntity<Void> savePassenger(@RequestBody PassengerDTO passenger);

    @PutMapping("/{id}")
    ResponseEntity<Void> updatePassanger(@PathVariable long id, @RequestBody PassengerDTO passenger);

    @DeleteMapping("/{id}")
    ResponseEntity<Void> deletePassenger(@PathVariable long id);
}
