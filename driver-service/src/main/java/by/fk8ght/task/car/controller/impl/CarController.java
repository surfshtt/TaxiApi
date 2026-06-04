package by.fk8ght.task.car.controller.impl;

import by.fk8ght.task.car.controller.CarApi;
import by.fk8ght.task.car.model.dto.CarDTO;
import by.fk8ght.task.car.service.CarService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class CarController implements CarApi {
    private final CarService carService;

    @Override
    public ResponseEntity<Page<CarDTO>> getCars(Pageable pageable) {
        return ResponseEntity.ok(carService.getCars(pageable));
    }

    @Override
    public ResponseEntity<CarDTO> getCar(long id) {
        return ResponseEntity.ok(carService.getCar(id));
    }

    @Override
    public ResponseEntity<Void> saveCar(@Valid CarDTO car) {
        carService.save(car);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Override
    public ResponseEntity<Void> updateCar(long id, @Valid CarDTO car) {
        carService.update(id, car);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Override
    public ResponseEntity<Void> deleteCar(long id) {
        carService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
