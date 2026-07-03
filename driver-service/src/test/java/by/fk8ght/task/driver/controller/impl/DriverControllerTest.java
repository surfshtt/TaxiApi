package by.fk8ght.task.driver.controller.impl;

import by.fk8ght.task.driver.model.dto.DriverDTO;
import by.fk8ght.task.driver.service.DriverService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DriverControllerTest {

    @Mock
    private DriverService driverService;

    @InjectMocks
    private DriverController controller;

    @Test
    void getDriversTest() {
        DriverDTO dto = new DriverDTO();
        dto.setId(1L);
        dto.setName("John");

        Page<DriverDTO> page = new PageImpl<>(List.of(dto));

        when(driverService.getDrivers(any(PageRequest.class))).thenReturn(page);

        ResponseEntity<Page<DriverDTO>> response = controller.getDrivers(PageRequest.of(0, 10));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().getTotalElements());
    }

    @Test
    void getDriverTest() {
        DriverDTO dto = new DriverDTO();
        dto.setId(1L);
        dto.setName("John");

        when(driverService.getDriver(1L)).thenReturn(dto);

        ResponseEntity<DriverDTO> response = controller.getDriver(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1L, response.getBody().getId());
    }

    @Test
    void saveDriverTest() {
        DriverDTO dto = new DriverDTO();
        dto.setName("John");
        dto.setEmail("john@example.com");
        dto.setPhone("123456789");

        doNothing().when(driverService).save(any(DriverDTO.class));

        ResponseEntity<Void> response = controller.saveDriver(dto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        verify(driverService).save(dto);
    }

    @Test
    void updateDriverTest() {
        DriverDTO dto = new DriverDTO();
        dto.setName("John Updated");

        doNothing().when(driverService).update(eq(1L), any(DriverDTO.class));

        ResponseEntity<Void> response = controller.updateDriver(1L, dto);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(driverService).update(1L, dto);
    }

    @Test
    void deleteDriverTest() {
        doNothing().when(driverService).delete(1L);

        ResponseEntity<Void> response = controller.deleteDriver(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(driverService).delete(1L);
    }
}
