package by.fk8ght.task.car.service.impl;

import by.fk8ght.task.car.model.dto.CarDTO;
import by.fk8ght.task.car.model.entity.Car;
import by.fk8ght.task.car.model.mapper.CarMapper;
import by.fk8ght.task.car.repository.CarRepository;
import by.fk8ght.task.driver.model.entity.Driver;
import by.fk8ght.task.driver.repository.DriverRepository;
import by.fk8ght.task.exception.BadRequestException;
import by.fk8ght.task.exception.ConflictException;
import by.fk8ght.task.exception.NotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CarServiceImplTest {

    @Mock
    private CarRepository repository;

    @Mock
    private DriverRepository driverRepository;

    @Mock
    private CarMapper mapper;

    @InjectMocks
    private CarServiceImpl service;

    @Test
    void getCarsTest() {
        Car car = new Car();
        car.setId(1L);
        car.setColor("Red");
        car.setBrand("Toyota");

        CarDTO dto = new CarDTO();
        dto.setId(1L);
        dto.setColor("Red");
        dto.setBrand("Toyota");

        when(repository.findAllNotDeleted(any(PageRequest.class)))
                .thenReturn(new PageImpl<>(java.util.List.of(car)));
        when(mapper.toDto(car)).thenReturn(dto);

        Page<CarDTO> result = service.getCars(PageRequest.of(0, 10));

        assertEquals(1, result.getTotalElements());
        assertEquals("Red", result.getContent().get(0).getColor());
    }

    @Test
    void getCarTest() {
        Car car = new Car();
        car.setId(1L);
        car.setColor("Red");
        car.setBrand("Toyota");

        CarDTO dto = new CarDTO();
        dto.setId(1L);
        dto.setColor("Red");
        dto.setBrand("Toyota");

        when(repository.findById(1L)).thenReturn(Optional.of(car));
        when(mapper.toDto(car)).thenReturn(dto);

        CarDTO result = service.getCar(1L);

        assertEquals(1L, result.getId());
        assertEquals("Red", result.getColor());
    }

    @Test
    void getCarNotFoundTest() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> service.getCar(1L));
    }

    @Test
    void getCarDeletedTest() {
        Car car = new Car();
        car.setId(1L);
        car.setDeleted(true);

        when(repository.findById(1L)).thenReturn(Optional.of(car));

        assertThrows(ConflictException.class, () -> service.getCar(1L));
    }

    @Test
    void saveCarTest() {
        CarDTO dto = new CarDTO();
        dto.setColor("Red");
        dto.setBrand("Toyota");
        dto.setPlateNumber("1234 AB-1");
        dto.setDriverId(1L);

        Driver driver = new Driver();
        driver.setId(1L);
        driver.setDeleted(false);

        Car car = new Car();
        car.setColor("Red");
        car.setBrand("Toyota");
        car.setPlateNumber("1234 AB-1");

        when(repository.existsByPlateNumber("1234 AB-1")).thenReturn(false);
        when(driverRepository.findById(1L)).thenReturn(Optional.of(driver));
        when(mapper.toEntity(dto)).thenReturn(car);
        when(repository.save(car)).thenReturn(car);

        service.save(dto);

        assertEquals(driver, car.getDriver());
        verify(repository).save(car);
    }

    @Test
    void saveCarDuplicatePlateNumberTest() {
        CarDTO dto = new CarDTO();
        dto.setPlateNumber("1234 AB-1");

        when(repository.existsByPlateNumber("1234 AB-1")).thenReturn(true);

        assertThrows(BadRequestException.class, () -> service.save(dto));
    }

    @Test
    void saveCarDriverNotFoundTest() {
        CarDTO dto = new CarDTO();
        dto.setPlateNumber("1234 AB-1");
        dto.setDriverId(1L);

        when(repository.existsByPlateNumber("1234 AB-1")).thenReturn(false);
        when(driverRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> service.save(dto));
    }

    @Test
    void saveCarDriverDeletedTest() {
        CarDTO dto = new CarDTO();
        dto.setPlateNumber("1234 AB-1");
        dto.setDriverId(1L);

        Driver driver = new Driver();
        driver.setId(1L);
        driver.setDeleted(true);

        when(repository.existsByPlateNumber("1234 AB-1")).thenReturn(false);
        when(driverRepository.findById(1L)).thenReturn(Optional.of(driver));

        assertThrows(ConflictException.class, () -> service.save(dto));
    }

    @Test
    void updateCarTest() {
        CarDTO dto = new CarDTO();
        dto.setColor("Blue");
        dto.setBrand("BMW");
        dto.setPlateNumber("5678 CD-2");
        dto.setDriverId(1L);

        Car car = new Car();
        car.setId(1L);
        car.setColor("Red");
        car.setBrand("Toyota");

        Driver driver = new Driver();
        driver.setId(1L);
        driver.setDeleted(false);

        when(repository.findById(1L)).thenReturn(Optional.of(car));
        when(driverRepository.findById(1L)).thenReturn(Optional.of(driver));
        when(repository.save(car)).thenReturn(car);

        service.update(1L, dto);

        assertEquals("Blue", car.getColor());
        assertEquals("BMW", car.getBrand());
        assertEquals("5678 CD-2", car.getPlateNumber());
        assertEquals(driver, car.getDriver());
        verify(repository).save(car);
    }

    @Test
    void updateCarNotFoundTest() {
        CarDTO dto = new CarDTO();

        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> service.update(1L, dto));
    }

    @Test
    void updateCarDeletedTest() {
        CarDTO dto = new CarDTO();

        Car car = new Car();
        car.setId(1L);
        car.setDeleted(true);

        when(repository.findById(1L)).thenReturn(Optional.of(car));

        assertThrows(ConflictException.class, () -> service.update(1L, dto));
    }

    @Test
    void deleteCarTest() {
        Car car = new Car();
        car.setId(1L);
        car.setDeleted(false);

        when(repository.findById(1L)).thenReturn(Optional.of(car));
        when(repository.save(car)).thenReturn(car);

        service.delete(1L);

        assertTrue(car.isDeleted());
        verify(repository).save(car);
    }

    @Test
    void deleteCarNotFoundTest() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> service.delete(1L));
    }

    @Test
    void deleteCarAlreadyDeletedTest() {
        Car car = new Car();
        car.setId(1L);
        car.setDeleted(true);

        when(repository.findById(1L)).thenReturn(Optional.of(car));

        assertThrows(ConflictException.class, () -> service.delete(1L));
    }
}
