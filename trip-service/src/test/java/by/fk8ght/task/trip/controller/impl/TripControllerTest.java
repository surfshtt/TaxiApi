package by.fk8ght.task.trip.controller.impl;

import by.fk8ght.task.trip.model.dto.TripDTO;
import by.fk8ght.task.trip.model.dto.TripStatusDTO;
import by.fk8ght.task.trip.model.entity.TripStatus;
import by.fk8ght.task.trip.service.TripService;
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

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TripControllerTest {

    @Mock
    private TripService tripService;

    @InjectMocks
    private TripController controller;

    @Test
    void getTripsTest() {
        TripDTO dto = new TripDTO();
        dto.setId(1L);
        dto.setDriverId(10L);

        Page<TripDTO> page = new PageImpl<>(List.of(dto));

        when(tripService.getTrips(any(PageRequest.class))).thenReturn(page);

        ResponseEntity<Page<TripDTO>> response = controller.getTrips(PageRequest.of(0, 10));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().getTotalElements());
    }

    @Test
    void getTripTest() {
        TripDTO dto = new TripDTO();
        dto.setId(1L);
        dto.setDriverId(10L);

        when(tripService.getTrip(1L)).thenReturn(dto);

        ResponseEntity<TripDTO> response = controller.getTrip(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1L, response.getBody().getId());
    }

    @Test
    void saveTripTest() {
        TripDTO dto = new TripDTO();
        dto.setDriverId(10L);
        dto.setPassengerId(20L);
        dto.setPickupAddress("Address 1");
        dto.setDestinationAddress("Address 2");
        dto.setCost(new BigDecimal("10.00"));

        doNothing().when(tripService).save(any(TripDTO.class));

        ResponseEntity<Void> response = controller.saveTrip(dto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        verify(tripService).save(dto);
    }

    @Test
    void updateTripTest() {
        TripDTO dto = new TripDTO();
        dto.setDriverId(10L);
        dto.setPassengerId(20L);

        doNothing().when(tripService).update(eq(1L), any(TripDTO.class));

        ResponseEntity<Void> response = controller.updateTrip(1L, dto);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(tripService).update(1L, dto);
    }

    @Test
    void deleteTripTest() {
        doNothing().when(tripService).delete(1L);

        ResponseEntity<Void> response = controller.deleteTrip(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(tripService).delete(1L);
    }

    @Test
    void updateTripStatusTest() {
        TripStatusDTO statusDto = new TripStatusDTO();
        statusDto.setStatus(TripStatus.COMPLETED);

        doNothing().when(tripService).updateStatus(eq(1L), any(TripStatus.class));

        ResponseEntity<Void> response = controller.updateTripStatus(1L, statusDto);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(tripService).updateStatus(1L, TripStatus.COMPLETED);
    }
}
