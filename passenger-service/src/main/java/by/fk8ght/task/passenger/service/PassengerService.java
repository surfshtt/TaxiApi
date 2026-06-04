package by.fk8ght.task.passenger.service;


import by.fk8ght.task.passenger.model.dto.PassengerDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PassengerService {
    Page<PassengerDTO> getPassengers(Pageable pageable);
    PassengerDTO getPassenger(long id);
    void save(PassengerDTO passengerDTO);
    void update(long id, PassengerDTO passengerDTO);
    void delete(long id);
}
