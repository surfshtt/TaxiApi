package by.fk8ght.task.rating.service;

import by.fk8ght.task.rating.model.dto.CreateRatingDTO;
import by.fk8ght.task.rating.model.dto.RatingDTO;
import by.fk8ght.task.rating.model.event.TripCompleted;

import java.util.List;

public interface RatingService {
    RatingDTO rateByPassenger(long tripId, CreateRatingDTO dto);
    RatingDTO rateByDriver(long tripId, CreateRatingDTO dto);
    List<RatingDTO> getTripRatings(long tripId);
    void consumersSignal(TripCompleted event);
}
