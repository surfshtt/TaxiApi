package by.fk8ght.task.driver.service.impl;

import by.fk8ght.task.driver.model.dto.DriverDTO;
import by.fk8ght.task.driver.model.entity.Driver;
import by.fk8ght.task.driver.model.mapper.DriverMapper;
import by.fk8ght.task.driver.repository.DriverRepository;
import by.fk8ght.task.driver.service.DriverService;
import by.fk8ght.task.exception.BadRequestException;
import by.fk8ght.task.exception.ConflictException;
import by.fk8ght.task.exception.NotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class DriverServiceImpl implements DriverService {
    private final DriverRepository repository;
    private final DriverMapper mapper;

    @Override
    public Page<DriverDTO> getDrivers(Pageable pageable) {
        return repository.findAllNotDeleted(pageable).map(mapper::toDto);
    }

    @Override
    public DriverDTO getDriver(long id) {
        Driver driver = repository.findById(id).orElseThrow(() -> new NotFoundException("Driver wasn't found"));

        if (driver.isDeleted()) {
            throw new ConflictException("Driver is deleted");
        }

        return mapper.toDto(driver);
    }

    @Transactional
    @Override
    public void save(DriverDTO driverDTO) {
        if (repository.existsByEmail(driverDTO.getEmail())) {
            throw new BadRequestException("Email already exists");
        }
        repository.save(mapper.toEntity(driverDTO));
    }

    @Transactional
    @Override
    public void update(long id, DriverDTO driverDTO) {
        Driver driver = repository.findById(id).orElseThrow(() -> new NotFoundException("Driver wasn't found"));

        if (driver.isDeleted()) {
            throw new ConflictException("Driver is deleted");
        }

        driver.setEmail(driverDTO.getEmail());
        driver.setName(driverDTO.getName());
        driver.setPhone(driverDTO.getPhone());
    }

    @Transactional
    @Override
    public void delete(long id) {
        Driver driver = repository.findById(id).orElseThrow(() -> new NotFoundException("Driver wasn't found"));

        if (driver.isDeleted()) {
            throw new ConflictException("Driver is already deleted");
        }

        driver.setDeleted(true);
    }
}
