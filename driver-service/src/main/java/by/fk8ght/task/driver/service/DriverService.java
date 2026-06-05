package by.fk8ght.task.driver.service;

import by.fk8ght.task.driver.model.dto.DriverDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DriverService {
    Page<DriverDTO> getDrivers(Pageable pageable);
    DriverDTO getDriver(long id);
    void save(DriverDTO driverDTO);
    void update(long id, DriverDTO driverDTO);
    void delete(long id);
}
