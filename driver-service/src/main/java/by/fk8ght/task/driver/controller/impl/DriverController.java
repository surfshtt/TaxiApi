package by.fk8ght.task.driver.controller.impl;

import by.fk8ght.task.driver.controller.DriverApi;
import by.fk8ght.task.driver.model.dto.DriverDTO;
import by.fk8ght.task.driver.service.DriverService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class DriverController implements DriverApi {
    private final DriverService driverService;

    @Override
    public ResponseEntity<Page<DriverDTO>> getDrivers(Pageable pageable) {
        return ResponseEntity.ok(driverService.getDrivers(pageable));
    }

    @Override
    public ResponseEntity<DriverDTO> getDriver(long id) {
        return ResponseEntity.ok(driverService.getDriver(id));
    }

    @Override
    public ResponseEntity<Void> saveDriver(@Valid DriverDTO driver) {
        driverService.save(driver);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Override
    public ResponseEntity<Void> updateDriver(long id, @Valid DriverDTO driver) {
        driverService.update(id, driver);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Override
    public ResponseEntity<Void> deleteDriver(long id) {
        driverService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
