package by.fk8ght.task.trip.service.impl;

import by.fk8ght.task.driver.model.entity.Driver;
import by.fk8ght.task.driver.repository.DriverRepository;
import by.fk8ght.task.exception.ConflictException;
import by.fk8ght.task.exception.NotFoundException;
import by.fk8ght.task.passenger.model.entity.Passenger;
import by.fk8ght.task.passenger.repository.PassengerRepository;
import by.fk8ght.task.trip.model.dto.TripDTO;
import by.fk8ght.task.trip.model.entity.Trip;
import by.fk8ght.task.trip.model.entity.TripStatus;
import by.fk8ght.task.trip.model.mapper.TripMapper;
import by.fk8ght.task.trip.repository.TripRepository;
import by.fk8ght.task.trip.service.TripService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class TripServiceImpl implements TripService {
    private final TripRepository repository;
    private final DriverRepository driverRepository;
    private final PassengerRepository passengerRepository;
    private final TripMapper mapper;

    @Override
    public Page<TripDTO> getTrips(Pageable pageable) {
        return repository.findAll(pageable).map(mapper::toDto);
    }

    @Override
    public TripDTO getTrip(long id) {
        Trip trip = repository.findById(id).orElseThrow(() -> new NotFoundException("Trip wasn't found"));
        return mapper.toDto(trip);
    }

    @Transactional
    @Override
    public void save(TripDTO tripDTO) {
        validateParticipants(tripDTO.getDriverId(), tripDTO.getPassengerId());
        Trip trip = mapper.toEntity(tripDTO);
        trip.setStatus(TripStatus.CREATED);
        trip.setOrderedAt(tripDTO.getOrderedAt() != null ? tripDTO.getOrderedAt() : LocalDateTime.now());
        repository.save(trip);
    }

    @Transactional
    @Override
    public void update(long id, TripDTO tripDTO) {
        Trip trip = repository.findById(id).orElseThrow(() -> new NotFoundException("Trip wasn't found"));
        validateParticipants(tripDTO.getDriverId(), tripDTO.getPassengerId());

        trip.setDriverId(tripDTO.getDriverId());
        trip.setPassengerId(tripDTO.getPassengerId());
        trip.setPickupAddress(tripDTO.getPickupAddress());
        trip.setDestinationAddress(tripDTO.getDestinationAddress());
        trip.setCost(tripDTO.getCost());

        if (tripDTO.getOrderedAt() != null) {
            trip.setOrderedAt(tripDTO.getOrderedAt());
        }
        if (tripDTO.getStatus() != null) {
            trip.setStatus(tripDTO.getStatus());
        }
    }

    @Transactional
    @Override
    public void delete(long id) {
        if (!repository.existsById(id)) {
            throw new NotFoundException("Trip wasn't found");
        }
        repository.deleteById(id);
    }

    @Transactional
    @Override
    public void updateStatus(long id, TripStatus status) {
        Trip trip = repository.findById(id).orElseThrow(() -> new NotFoundException("Trip wasn't found"));
        trip.setStatus(status);
    }

    private void validateParticipants(long driverId, long passengerId) {
        Driver driver = driverRepository.findById(driverId).orElseThrow(() -> new NotFoundException("Driver wasn't found"));
        if (driver.isDeleted()) {
            throw new ConflictException("Driver is deleted");
        }

        Passenger passenger = passengerRepository.findById(passengerId).orElseThrow(() -> new NotFoundException("Passenger wasn't found"));
        if (passenger.isDeleted()) {
            throw new ConflictException("Passenger is deleted");
        }
    }
}
