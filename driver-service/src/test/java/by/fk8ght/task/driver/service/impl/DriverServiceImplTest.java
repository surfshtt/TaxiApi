package by.fk8ght.task.driver.service.impl;

import by.fk8ght.task.driver.model.dto.DriverDTO;
import by.fk8ght.task.driver.model.entity.Driver;
import by.fk8ght.task.driver.model.mapper.DriverMapper;
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
class DriverServiceImplTest {

    @Mock
    private DriverRepository repository;

    @Mock
    private DriverMapper mapper;

    @InjectMocks
    private DriverServiceImpl service;

    @Test
    void getDriversTest() {
        Driver driver = new Driver();
        driver.setId(1L);
        driver.setName("John");
        driver.setEmail("john@example.com");
        driver.setPhone("123456789");

        DriverDTO dto = new DriverDTO();
        dto.setId(1L);
        dto.setName("John");
        dto.setEmail("john@example.com");
        dto.setPhone("123456789");

        when(repository.findAllNotDeleted(any(PageRequest.class)))
                .thenReturn(new PageImpl<>(java.util.List.of(driver)));
        when(mapper.toDto(driver)).thenReturn(dto);

        Page<DriverDTO> result = service.getDrivers(PageRequest.of(0, 10));

        assertEquals(1, result.getTotalElements());
        assertEquals("John", result.getContent().get(0).getName());
    }

    @Test
    void getDriverTest() {
        Driver driver = new Driver();
        driver.setId(1L);
        driver.setName("John");
        driver.setEmail("john@example.com");
        driver.setPhone("123456789");

        DriverDTO dto = new DriverDTO();
        dto.setId(1L);
        dto.setName("John");
        dto.setEmail("john@example.com");
        dto.setPhone("123456789");

        when(repository.findById(1L)).thenReturn(Optional.of(driver));
        when(mapper.toDto(driver)).thenReturn(dto);

        DriverDTO result = service.getDriver(1L);

        assertEquals(1L, result.getId());
        assertEquals("John", result.getName());
    }

    @Test
    void getDriverNotFoundTest() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> service.getDriver(1L));
    }

    @Test
    void getDriverDeletedTest() {
        Driver driver = new Driver();
        driver.setId(1L);
        driver.setDeleted(true);

        when(repository.findById(1L)).thenReturn(Optional.of(driver));

        assertThrows(ConflictException.class, () -> service.getDriver(1L));
    }

    @Test
    void saveDriverTest() {
        DriverDTO dto = new DriverDTO();
        dto.setName("John");
        dto.setEmail("john@example.com");
        dto.setPhone("123456789");

        Driver driver = new Driver();
        driver.setName("John");
        driver.setEmail("john@example.com");
        driver.setPhone("123456789");

        when(repository.existsByEmail("john@example.com")).thenReturn(false);
        when(mapper.toEntity(dto)).thenReturn(driver);
        when(repository.save(driver)).thenReturn(driver);

        service.save(dto);

        verify(repository).save(driver);
    }

    @Test
    void saveDriverDuplicateEmailTest() {
        DriverDTO dto = new DriverDTO();
        dto.setEmail("john@example.com");

        when(repository.existsByEmail("john@example.com")).thenReturn(true);

        assertThrows(BadRequestException.class, () -> service.save(dto));
    }

    @Test
    void updateDriverTest() {
        DriverDTO dto = new DriverDTO();
        dto.setName("John Updated");
        dto.setEmail("john.updated@example.com");
        dto.setPhone("987654321");

        Driver driver = new Driver();
        driver.setId(1L);
        driver.setName("John");
        driver.setEmail("john@example.com");
        driver.setPhone("123456789");

        when(repository.findById(1L)).thenReturn(Optional.of(driver));
        when(repository.save(driver)).thenReturn(driver);

        service.update(1L, dto);

        assertEquals("John Updated", driver.getName());
        assertEquals("john.updated@example.com", driver.getEmail());
        assertEquals("987654321", driver.getPhone());
        verify(repository).save(driver);
    }

    @Test
    void updateDriverNotFoundTest() {
        DriverDTO dto = new DriverDTO();

        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> service.update(1L, dto));
    }

    @Test
    void updateDriverDeletedTest() {
        DriverDTO dto = new DriverDTO();

        Driver driver = new Driver();
        driver.setId(1L);
        driver.setDeleted(true);

        when(repository.findById(1L)).thenReturn(Optional.of(driver));

        assertThrows(ConflictException.class, () -> service.update(1L, dto));
    }

    @Test
    void deleteDriverTest() {
        Driver driver = new Driver();
        driver.setId(1L);
        driver.setDeleted(false);

        when(repository.findById(1L)).thenReturn(Optional.of(driver));
        when(repository.save(driver)).thenReturn(driver);

        service.delete(1L);

        assertTrue(driver.isDeleted());
        verify(repository).save(driver);
    }

    @Test
    void deleteDriverNotFoundTest() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> service.delete(1L));
    }

    @Test
    void deleteDriverAlreadyDeletedTest() {
        Driver driver = new Driver();
        driver.setId(1L);
        driver.setDeleted(true);

        when(repository.findById(1L)).thenReturn(Optional.of(driver));

        assertThrows(ConflictException.class, () -> service.delete(1L));
    }
}
