package by.fk8ght.task.car.controller;

import by.fk8ght.task.car.model.dto.CarDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("api/car")
public interface CarApi {
    @GetMapping
    ResponseEntity<Page<CarDTO>> getCars(@PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable);

    @GetMapping("/{id}")
    ResponseEntity<CarDTO> getCar(@PathVariable long id);

    @PutMapping
    ResponseEntity<Void> saveCar(@RequestBody CarDTO car);

    @PutMapping("/{id}")
    ResponseEntity<Void> updateCar(@PathVariable long id, @RequestBody CarDTO car);

    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteCar(@PathVariable long id);
}
