package by.fk8ght.task.passenger.controller.impl;

import by.fk8ght.task.passenger.model.dto.PassengerDTO;
import by.fk8ght.task.passenger.service.PassengerService;
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
class PassengerControllerTest {

    @Mock
    private PassengerService passengerService;

    @InjectMocks
    private PassengerController controller;

    @Test
    void getPassengersTest() {
        PassengerDTO dto = new PassengerDTO();
        dto.setId(1L);
        dto.setName("John");

        Page<PassengerDTO> page = new PageImpl<>(List.of(dto));

        when(passengerService.getPassengers(any(PageRequest.class))).thenReturn(page);

        ResponseEntity<Page<PassengerDTO>> response = controller.getPassengers(PageRequest.of(0, 10));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().getTotalElements());
    }

    @Test
    void getPassengerTest() {
        PassengerDTO dto = new PassengerDTO();
        dto.setId(1L);
        dto.setName("John");

        when(passengerService.getPassenger(1L)).thenReturn(dto);

        ResponseEntity<PassengerDTO> response = controller.getPassenger(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1L, response.getBody().getId());
    }

    @Test
    void savePassengerTest() {
        PassengerDTO dto = new PassengerDTO();
        dto.setName("John");
        dto.setEmail("john@example.com");
        dto.setPhone("123456789");

        doNothing().when(passengerService).save(any(PassengerDTO.class));

        ResponseEntity<Void> response = controller.savePassenger(dto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        verify(passengerService).save(dto);
    }

    @Test
    void updatePassengerTest() {
        PassengerDTO dto = new PassengerDTO();
        dto.setName("John Updated");

        doNothing().when(passengerService).update(eq(1L), any(PassengerDTO.class));

        ResponseEntity<Void> response = controller.updatePassanger(1L, dto);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(passengerService).update(1L, dto);
    }

    @Test
    void deletePassengerTest() {
        doNothing().when(passengerService).delete(1L);

        ResponseEntity<Void> response = controller.deletePassenger(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(passengerService).delete(1L);
    }
}
