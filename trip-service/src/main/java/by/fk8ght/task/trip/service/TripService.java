package by.fk8ght.task.trip.service;

import by.fk8ght.task.trip.model.dto.TripDTO;
import by.fk8ght.task.trip.model.entity.TripStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TripService {
    Page<TripDTO> getTrips(Pageable pageable);
    TripDTO getTrip(long id);
    void save(TripDTO tripDTO);
    void update(long id, TripDTO tripDTO);
    void delete(long id);
    void updateStatus(long id, TripStatus status);
}
