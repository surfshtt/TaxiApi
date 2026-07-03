package by.fk8ght.task.car.controller.impl;

import by.fk8ght.task.car.model.dto.CarDTO;
import by.fk8ght.task.car.service.CarService;
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
class CarControllerTest {

    @Mock
    private CarService carService;

    @InjectMocks
    private CarController controller;

    @Test
    void getCarsTest() {
        CarDTO dto = new CarDTO();
        dto.setId(1L);
        dto.setColor("Red");

        Page<CarDTO> page = new PageImpl<>(List.of(dto));

        when(carService.getCars(any(PageRequest.class))).thenReturn(page);

        ResponseEntity<Page<CarDTO>> response = controller.getCars(PageRequest.of(0, 10));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().getTotalElements());
    }

    @Test
    void getCarTest() {
        CarDTO dto = new CarDTO();
        dto.setId(1L);
        dto.setColor("Red");

        when(carService.getCar(1L)).thenReturn(dto);

        ResponseEntity<CarDTO> response = controller.getCar(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1L, response.getBody().getId());
    }

    @Test
    void saveCarTest() {
        CarDTO dto = new CarDTO();
        dto.setColor("Red");
        dto.setBrand("Toyota");
        dto.setPlateNumber("1234 AB-1");
        dto.setDriverId(1L);

        doNothing().when(carService).save(any(CarDTO.class));

        ResponseEntity<Void> response = controller.saveCar(dto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        verify(carService).save(dto);
    }

    @Test
    void updateCarTest() {
        CarDTO dto = new CarDTO();
        dto.setColor("Blue");

        doNothing().when(carService).update(eq(1L), any(CarDTO.class));

        ResponseEntity<Void> response = controller.updateCar(1L, dto);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(carService).update(1L, dto);
    }

    @Test
    void deleteCarTest() {
        doNothing().when(carService).delete(1L);

        ResponseEntity<Void> response = controller.deleteCar(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(carService).delete(1L);
    }
}
