package by.fk8ght.task.trip.service.impl;

import by.fk8ght.task.exception.NotFoundException;
import by.fk8ght.task.trip.client.DriverClient;
import by.fk8ght.task.trip.client.PassengerClient;
import by.fk8ght.task.trip.model.dto.DriverDTO;
import by.fk8ght.task.trip.model.dto.PassengerDTO;
import by.fk8ght.task.trip.model.dto.TripDTO;
import by.fk8ght.task.trip.model.entity.Trip;
import by.fk8ght.task.trip.model.entity.TripStatus;
import by.fk8ght.task.trip.model.mapper.TripMapper;
import by.fk8ght.task.trip.repository.TripRepository;
import by.fk8ght.task.trip.service.TripService;
import feign.FeignException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.kafka.core.KafkaTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TripServiceImplTest {

    @Mock
    private TripRepository repository;

    @Mock
    private DriverClient driverClient;

    @Mock
    private PassengerClient passengerClient;

    @Mock
    private TripMapper mapper;

    @Mock
    private KafkaTemplate<String, by.fk8ght.task.trip.model.event.TripCompleted> kafkaTemplate;

    @InjectMocks
    private TripServiceImpl service;

    @Test
    void getTripsTest() {
        Trip trip = new Trip();
        trip.setId(1L);
        trip.setDriverId(10L);
        trip.setPassengerId(20L);

        TripDTO dto = new TripDTO();
        dto.setId(1L);
        dto.setDriverId(10L);
        dto.setPassengerId(20L);

        when(repository.findAll(any(PageRequest.class)))
                .thenReturn(new PageImpl<>(java.util.List.of(trip)));
        when(mapper.toDto(trip)).thenReturn(dto);

        Page<TripDTO> result = service.getTrips(PageRequest.of(0, 10));

        assertEquals(1, result.getTotalElements());
        assertEquals(1L, result.getContent().get(0).getId());
    }

    @Test
    void getTripTest() {
        Trip trip = new Trip();
        trip.setId(1L);
        trip.setDriverId(10L);
        trip.setPassengerId(20L);

        TripDTO dto = new TripDTO();
        dto.setId(1L);
        dto.setDriverId(10L);
        dto.setPassengerId(20L);

        when(repository.findById(1L)).thenReturn(Optional.of(trip));
        when(mapper.toDto(trip)).thenReturn(dto);

        TripDTO result = service.getTrip(1L);

        assertEquals(1L, result.getId());
        assertEquals(10L, result.getDriverId());
    }

    @Test
    void getTripNotFoundTest() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> service.getTrip(1L));
    }

    @Test
    void saveTripTest() {
        TripDTO dto = new TripDTO();
        dto.setDriverId(10L);
        dto.setPassengerId(20L);
        dto.setPickupAddress("Address 1");
        dto.setDestinationAddress("Address 2");
        dto.setCost(new BigDecimal("10.00"));

        DriverDTO driverDto = new DriverDTO();
        driverDto.setId(10L);

        PassengerDTO passengerDto = new PassengerDTO();
        passengerDto.setId(20L);

        Trip trip = new Trip();
        trip.setDriverId(10L);
        trip.setPassengerId(20L);
        trip.setPickupAddress("Address 1");
        trip.setDestinationAddress("Address 2");
        trip.setCost(new BigDecimal("10.00"));
        trip.setStatus(TripStatus.CREATED);

        when(driverClient.getDriver(10L)).thenReturn(driverDto);
        when(passengerClient.getPassenger(20L)).thenReturn(passengerDto);
        when(mapper.toEntity(dto)).thenReturn(trip);
        when(repository.save(trip)).thenReturn(trip);

        service.save(dto);

        assertEquals(TripStatus.CREATED, trip.getStatus());
        assertNotNull(trip.getOrderedAt());
        verify(repository).save(trip);
    }

    @Test
    void saveTripDriverNotFoundTest() {
        TripDTO dto = new TripDTO();
        dto.setDriverId(10L);
        dto.setPassengerId(20L);

        FeignException.NotFound notFound = mock(FeignException.NotFound.class);
        when(driverClient.getDriver(10L)).thenThrow(notFound);

        assertThrows(NotFoundException.class, () -> service.save(dto));
    }

    @Test
    void saveTripPassengerNotFoundTest() {
        TripDTO dto = new TripDTO();
        dto.setDriverId(10L);
        dto.setPassengerId(20L);

        DriverDTO driverDto = new DriverDTO();
        driverDto.setId(10L);

        FeignException.NotFound notFound = mock(FeignException.NotFound.class);
        when(driverClient.getDriver(10L)).thenReturn(driverDto);
        when(passengerClient.getPassenger(20L)).thenThrow(notFound);

        assertThrows(NotFoundException.class, () -> service.save(dto));
    }

    @Test
    void updateTripTest() {
        TripDTO dto = new TripDTO();
        dto.setDriverId(10L);
        dto.setPassengerId(20L);
        dto.setPickupAddress("New Address 1");
        dto.setDestinationAddress("New Address 2");
        dto.setCost(new BigDecimal("15.00"));

        DriverDTO driverDto = new DriverDTO();
        driverDto.setId(10L);

        PassengerDTO passengerDto = new PassengerDTO();
        passengerDto.setId(20L);

        Trip trip = new Trip();
        trip.setId(1L);
        trip.setDriverId(5L);
        trip.setPassengerId(15L);
        trip.setPickupAddress("Old Address 1");
        trip.setDestinationAddress("Old Address 2");
        trip.setCost(new BigDecimal("10.00"));

        when(repository.findById(1L)).thenReturn(Optional.of(trip));
        when(driverClient.getDriver(10L)).thenReturn(driverDto);
        when(passengerClient.getPassenger(20L)).thenReturn(passengerDto);
        when(repository.save(trip)).thenReturn(trip);

        service.update(1L, dto);

        assertEquals(10L, trip.getDriverId());
        assertEquals(20L, trip.getPassengerId());
        assertEquals("New Address 1", trip.getPickupAddress());
        assertEquals("New Address 2", trip.getDestinationAddress());
        assertEquals(new BigDecimal("15.00"), trip.getCost());
        verify(repository).save(trip);
    }

    @Test
    void updateTripNotFoundTest() {
        TripDTO dto = new TripDTO();

        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> service.update(1L, dto));
    }

    @Test
    void deleteTripTest() {
        when(repository.existsById(1L)).thenReturn(true);
        doNothing().when(repository).deleteById(1L);

        service.delete(1L);

        verify(repository).deleteById(1L);
    }

    @Test
    void deleteTripNotFoundTest() {
        when(repository.existsById(1L)).thenReturn(false);

        assertThrows(NotFoundException.class, () -> service.delete(1L));
    }

    @Test
    void updateTripStatusTest() {
        Trip trip = new Trip();
        trip.setId(1L);
        trip.setStatus(TripStatus.ON_WAY_TO_DESTINATION);

        when(repository.findById(1L)).thenReturn(Optional.of(trip));
        when(repository.save(trip)).thenReturn(trip);

        service.updateStatus(1L, TripStatus.COMPLETED);

        assertEquals(TripStatus.COMPLETED, trip.getStatus());
        verify(repository).save(trip);
        verify(kafkaTemplate).send(eq("trip-completed"), any(by.fk8ght.task.trip.model.event.TripCompleted.class));
    }

    @Test
    void updateTripStatusNotCompletedTest() {
        Trip trip = new Trip();
        trip.setId(1L);
        trip.setStatus(TripStatus.CREATED);

        when(repository.findById(1L)).thenReturn(Optional.of(trip));
        when(repository.save(trip)).thenReturn(trip);

        service.updateStatus(1L, TripStatus.ACCEPTED);

        assertEquals(TripStatus.ACCEPTED, trip.getStatus());
        verify(repository).save(trip);
        verify(kafkaTemplate, never()).send(eq("trip-completed"), any());
    }

    @Test
    void updateTripStatusNotFoundTest() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> service.updateStatus(1L, TripStatus.COMPLETED));
    }

    @Test
    void validateParticipantsFallbackTest() {
        Exception ex = new RuntimeException("Service unavailable");

        assertThrows(RuntimeException.class, () -> service.validateParticipantsFallback(1L, 2L, ex));
    }
}
