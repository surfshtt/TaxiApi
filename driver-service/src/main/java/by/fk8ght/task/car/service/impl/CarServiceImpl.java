package by.fk8ght.task.car.service.impl;

import by.fk8ght.task.car.model.dto.CarDTO;
import by.fk8ght.task.car.model.entity.Car;
import by.fk8ght.task.car.model.mapper.CarMapper;
import by.fk8ght.task.car.repository.CarRepository;
import by.fk8ght.task.car.service.CarService;
import by.fk8ght.task.driver.model.entity.Driver;
import by.fk8ght.task.driver.repository.DriverRepository;
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
public class CarServiceImpl implements CarService {
    private final CarRepository repository;
    private final DriverRepository driverRepository;
    private final CarMapper mapper;

    @Transactional
    @Override
    public Page<CarDTO> getCars(Pageable pageable) {
        return repository.findAllNotDeleted(pageable).map(mapper::toDto);
    }

    @Transactional
    @Override
    public CarDTO getCar(long id) {
        Car car = repository.findById(id).orElseThrow(() -> new NotFoundException("Car wasn't found"));

        if (car.isDeleted()) {
            throw new ConflictException("Car is deleted");
        }

        return mapper.toDto(car);
    }

    @Transactional
    @Override
    public void save(CarDTO carDTO) {
        if (repository.existsByPlateNumber(carDTO.getPlateNumber())) {
            throw new BadRequestException("Plate number already exists");
        }
        Car car = mapper.toEntity(carDTO);
        car.setDriver(findActiveDriver(carDTO.getDriverId()));
        repository.save(car);
    }

    @Transactional
    @Override
    public void update(long id, CarDTO carDTO) {
        Car car = repository.findById(id).orElseThrow(() -> new NotFoundException("Car wasn't found"));

        if (car.isDeleted()) {
            throw new ConflictException("Car is deleted");
        }

        car.setColor(carDTO.getColor());
        car.setBrand(carDTO.getBrand());
        car.setPlateNumber(carDTO.getPlateNumber());
        car.setDriver(findActiveDriver(carDTO.getDriverId()));
    }

    @Transactional
    @Override
    public void delete(long id) {
        Car car = repository.findById(id).orElseThrow(() -> new NotFoundException("Car wasn't found"));

        if (car.isDeleted()) {
            throw new ConflictException("Car is already deleted");
        }

        car.setDeleted(true);
    }

    private Driver findActiveDriver(long driverId) {
        Driver driver = driverRepository.findById(driverId).orElseThrow(() -> new NotFoundException("Driver wasn't found"));

        if (driver.isDeleted()) {
            throw new ConflictException("Driver is deleted");
        }

        return driver;
    }
}
