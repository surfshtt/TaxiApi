package by.fk8ght.task.trip.service.impl;

import by.fk8ght.task.exception.NotFoundException;
import by.fk8ght.task.trip.client.DriverClient;
import by.fk8ght.task.trip.client.PassengerClient;
import by.fk8ght.task.trip.model.dto.DriverDTO;
import by.fk8ght.task.trip.model.dto.PassengerDTO;
import by.fk8ght.task.trip.model.dto.TripDTO;
import by.fk8ght.task.trip.model.entity.Trip;
import by.fk8ght.task.trip.model.entity.TripStatus;
import by.fk8ght.task.trip.model.event.TripCompleted;
import by.fk8ght.task.trip.model.mapper.TripMapper;
import by.fk8ght.task.trip.repository.TripRepository;
import by.fk8ght.task.trip.service.TripService;
import feign.FeignException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class TripServiceImpl implements TripService {
    private final TripRepository repository;
    private final DriverClient driverClient;
    private final PassengerClient passengerClient;
    private final TripMapper mapper;
    private final KafkaTemplate<String, TripCompleted> kafkaTemplate;

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

        if(status == TripStatus.COMPLETED){
            kafkaTemplate.send("trip-completed", new TripCompleted(trip.getId(), trip.getDriverId(), trip.getPassengerId()));
        }
    }

    //я знаю что это глуповатое решение, не хотел на бизнес логике зацикливаться
    @CircuitBreaker(
            name = "participantValidation",
            fallbackMethod = "validateParticipantsFallback"
    )
    public void validateParticipants(long driverId, long passengerId) {
        try {
            driverClient.getDriver(driverId);
            passengerClient.getPassenger(passengerId);
        } catch (FeignException.NotFound ex) {
            throw new NotFoundException("Driver or Passenger wasn't found");
        }
    }

    public void validateParticipantsFallback(long driverId, long passengerId, Exception ex){
        throw new RuntimeException("Driver or passenger services aren't available now");
    }
}
