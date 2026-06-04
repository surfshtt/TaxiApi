package by.fk8ght.task.driver.controller;

import by.fk8ght.task.driver.model.dto.DriverDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("api/driver")
public interface DriverApi {
    @GetMapping
    ResponseEntity<Page<DriverDTO>> getDrivers(@PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable);

    @GetMapping("/{id}")
    ResponseEntity<DriverDTO> getDriver(@PathVariable long id);

    @PutMapping
    ResponseEntity<Void> saveDriver(@RequestBody DriverDTO driver);

    @PutMapping("/{id}")
    ResponseEntity<Void> updateDriver(@PathVariable long id, @RequestBody DriverDTO driver);

    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteDriver(@PathVariable long id);
}
