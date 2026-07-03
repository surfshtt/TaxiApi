package by.fk8ght.task.passenger.service.impl;

import by.fk8ght.task.exception.BadRequestException;
import by.fk8ght.task.exception.ConflictException;
import by.fk8ght.task.exception.NotFoundException;
import by.fk8ght.task.passenger.model.dto.PassengerDTO;
import by.fk8ght.task.passenger.model.entity.Passenger;
import by.fk8ght.task.passenger.model.mapper.PassengerMapper;
import by.fk8ght.task.passenger.repository.PassengerRepository;
import by.fk8ght.task.passenger.service.PassengerService;
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
class PassengerServiceImplTest {

    @Mock
    private PassengerRepository repository;

    @Mock
    private PassengerMapper mapper;

    @InjectMocks
    private PassengerServiceImpl service;

    @Test
    void getPassengersTest() {
        Passenger passenger = new Passenger();
        passenger.setId(1L);
        passenger.setName("John");
        passenger.setEmail("john@example.com");
        passenger.setPhone("123456789");

        PassengerDTO dto = new PassengerDTO();
        dto.setId(1L);
        dto.setName("John");
        dto.setEmail("john@example.com");
        dto.setPhone("123456789");

        when(repository.findAllNotDeleted(any(PageRequest.class)))
                .thenReturn(new PageImpl<>(java.util.List.of(passenger)));
        when(mapper.toDto(passenger)).thenReturn(dto);

        Page<PassengerDTO> result = service.getPassengers(PageRequest.of(0, 10));

        assertEquals(1, result.getTotalElements());
        assertEquals("John", result.getContent().get(0).getName());
    }

    @Test
    void getPassengerTest() {
        Passenger passenger = new Passenger();
        passenger.setId(1L);
        passenger.setName("John");
        passenger.setEmail("john@example.com");
        passenger.setPhone("123456789");

        PassengerDTO dto = new PassengerDTO();
        dto.setId(1L);
        dto.setName("John");
        dto.setEmail("john@example.com");
        dto.setPhone("123456789");

        when(repository.findById(1L)).thenReturn(Optional.of(passenger));
        when(mapper.toDto(passenger)).thenReturn(dto);

        PassengerDTO result = service.getPassenger(1L);

        assertEquals(1L, result.getId());
        assertEquals("John", result.getName());
    }

    @Test
    void getPassengerNotFoundTest() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> service.getPassenger(1L));
    }

    @Test
    void getPassengerDeletedTest() {
        Passenger passenger = new Passenger();
        passenger.setId(1L);
        passenger.setDeleted(true);

        when(repository.findById(1L)).thenReturn(Optional.of(passenger));

        assertThrows(ConflictException.class, () -> service.getPassenger(1L));
    }

    @Test
    void savePassengerTest() {
        PassengerDTO dto = new PassengerDTO();
        dto.setName("John");
        dto.setEmail("john@example.com");
        dto.setPhone("123456789");

        Passenger passenger = new Passenger();
        passenger.setName("John");
        passenger.setEmail("john@example.com");
        passenger.setPhone("123456789");

        when(repository.existsByEmail("john@example.com")).thenReturn(false);
        when(mapper.toEntity(dto)).thenReturn(passenger);
        when(repository.save(passenger)).thenReturn(passenger);

        service.save(dto);

        verify(repository).save(passenger);
    }

    @Test
    void savePassengerDuplicateEmailTest() {
        PassengerDTO dto = new PassengerDTO();
        dto.setEmail("john@example.com");

        when(repository.existsByEmail("john@example.com")).thenReturn(true);

        assertThrows(BadRequestException.class, () -> service.save(dto));
    }

    @Test
    void updatePassengerTest() {
        PassengerDTO dto = new PassengerDTO();
        dto.setName("John Updated");
        dto.setEmail("john.updated@example.com");
        dto.setPhone("987654321");

        Passenger passenger = new Passenger();
        passenger.setId(1L);
        passenger.setName("John");
        passenger.setEmail("john@example.com");
        passenger.setPhone("123456789");

        when(repository.findById(1L)).thenReturn(Optional.of(passenger));
        when(repository.save(passenger)).thenReturn(passenger);

        service.update(1L, dto);

        assertEquals("John Updated", passenger.getName());
        assertEquals("john.updated@example.com", passenger.getEmail());
        assertEquals("987654321", passenger.getPhone());
        verify(repository).save(passenger);
    }

    @Test
    void updatePassengerNotFoundTest() {
        PassengerDTO dto = new PassengerDTO();

        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> service.update(1L, dto));
    }

    @Test
    void updatePassengerDeletedTest() {
        PassengerDTO dto = new PassengerDTO();

        Passenger passenger = new Passenger();
        passenger.setId(1L);
        passenger.setDeleted(true);

        when(repository.findById(1L)).thenReturn(Optional.of(passenger));

        assertThrows(ConflictException.class, () -> service.update(1L, dto));
    }

    @Test
    void deletePassengerTest() {
        Passenger passenger = new Passenger();
        passenger.setId(1L);
        passenger.setDeleted(false);

        when(repository.findById(1L)).thenReturn(Optional.of(passenger));
        when(repository.save(passenger)).thenReturn(passenger);

        service.delete(1L);

        assertTrue(passenger.isDeleted());
        verify(repository).save(passenger);
    }

    @Test
    void deletePassengerNotFoundTest() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> service.delete(1L));
    }

    @Test
    void deletePassengerAlreadyDeletedTest() {
        Passenger passenger = new Passenger();
        passenger.setId(1L);
        passenger.setDeleted(true);

        when(repository.findById(1L)).thenReturn(Optional.of(passenger));

        assertThrows(ConflictException.class, () -> service.delete(1L));
    }
}
