package by.fk8ght.task.passenger.service.impl;

import by.fk8ght.task.exception.BadRequestException;
import by.fk8ght.task.exception.ConflictException;
import by.fk8ght.task.passenger.model.dto.PassengerDTO;
import by.fk8ght.task.passenger.model.entity.Passenger;
import by.fk8ght.task.passenger.model.mapper.PassengerMapper;
import by.fk8ght.task.passenger.repository.PassengerRepository;
import by.fk8ght.task.passenger.service.PassengerService;
import by.fk8ght.task.exception.NotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class PassengerServiceImpl implements PassengerService {
    private final PassengerRepository repository;
    private final PassengerMapper mapper;

    @Override
    public Page<PassengerDTO> getPassengers(Pageable pageable) {
        Page<Passenger> passengers = repository.findAllNotDeleted(pageable);
        return passengers.map(mapper::toDto);
    }

    @Override
    public PassengerDTO getPassenger(long id) {
        Passenger passenger = repository.findById(id).orElseThrow(() -> new NotFoundException("Passenger wasn't found"));

        if(passenger.isDeleted()){
            throw new ConflictException("User is deleted");
        }

        return mapper.toDto(passenger);
    }

    @Transactional
    @Override
    public void save(PassengerDTO passengerDTO) {
        if (repository.existsByEmail(passengerDTO.getEmail())) {
            throw new BadRequestException("Email already exists");
        }
        Passenger passenger = mapper.toEntity(passengerDTO);
        repository.save(passenger);
    }

    @Transactional
    @Override
    public void update(long id, PassengerDTO passengerDTO) {
        Passenger passenger = repository.findById(id).orElseThrow(() -> new NotFoundException("Passenger wasn't found"));

        if(passenger.isDeleted()){
            throw new ConflictException("User is deleted");
        }

        passenger.setEmail(passengerDTO.getEmail());
        passenger.setName(passengerDTO.getName());
        passenger.setPhone(passengerDTO.getPhone());
    }

    @Transactional
    @Override
    public void delete(long id) {
        Passenger passenger = repository.findById(id).orElseThrow(() -> new NotFoundException("Passenger wasn't found"));

        if(passenger.isDeleted()){
            throw new ConflictException("User is already deleted");
        }

        passenger.setDeleted(true);
    }
}
