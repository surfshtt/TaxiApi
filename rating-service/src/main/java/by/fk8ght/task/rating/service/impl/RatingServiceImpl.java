package by.fk8ght.task.rating.service.impl;

import by.fk8ght.task.exception.BadRequestException;
import by.fk8ght.task.exception.ConflictException;
import by.fk8ght.task.exception.NotFoundException;
import by.fk8ght.task.rating.client.TripClient;
import by.fk8ght.task.rating.model.dto.CreateRatingDTO;
import by.fk8ght.task.rating.model.dto.RatingDTO;
import by.fk8ght.task.rating.model.dto.TripDTO;
import by.fk8ght.task.rating.model.dto.TripStatus;
import by.fk8ght.task.rating.model.entity.RatedBy;
import by.fk8ght.task.rating.model.entity.Rating;
import by.fk8ght.task.rating.model.event.TripCompleted;
import by.fk8ght.task.rating.model.mapper.RatingMapper;
import by.fk8ght.task.rating.repository.RatingRepository;
import by.fk8ght.task.rating.service.RatingService;
import feign.FeignException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class RatingServiceImpl implements RatingService {
    private final RatingRepository repository;
    private final TripClient tripClient;
    private final RatingMapper mapper;

    @Transactional
    @Override
    public RatingDTO rateByPassenger(long tripId, CreateRatingDTO dto) {
        return saveRating(tripId, RatedBy.PASSENGER, dto);
    }

    @Transactional
    @Override
    public RatingDTO rateByDriver(long tripId, CreateRatingDTO dto) {
        return saveRating(tripId, RatedBy.DRIVER, dto);
    }

    @Override
    public List<RatingDTO> getTripRatings(long tripId) {
        validateAndGetTrip(tripId);
        return repository.findAllByTripId(tripId).stream().map(mapper::toDto).toList();
    }

    @Override
    public void consumersSignal(TripCompleted event){
        Rating rating1 = new Rating();
        rating1.setTripId(event.getTripId());
        rating1.setDriverId(event.getDriverId());
        rating1.setPassengerId(event.getPassengerId());
        rating1.setRatedBy(RatedBy.DRIVER);
        rating1.setScore(-1);

        Rating rating2 = new Rating();
        rating2.setTripId(event.getTripId());
        rating2.setDriverId(event.getDriverId());
        rating2.setPassengerId(event.getPassengerId());
        rating2.setRatedBy(RatedBy.PASSENGER);
        rating2.setScore(-1);

        repository.save(rating1);
        repository.save(rating2);
    }

    private RatingDTO saveRating(long tripId, RatedBy ratedBy, CreateRatingDTO dto) {
        TripDTO trip = validateAndGetTrip(tripId);

        if (trip.getStatus() != TripStatus.ACCEPTED) {
            throw new BadRequestException("Trip must be completed before rating");
        }

        Rating rating = repository.findByTripIdAndRatedBy(tripId, ratedBy).orElseThrow( () -> new BadRequestException("Rating doesn't exists"));
        rating.setScore(dto.getScore());
        rating.setComment(dto.getComment());

        return mapper.toDto(repository.save(rating));
    }

    //я знаю что это глуповатое решение, не хотел на бизнес логике зацикливаться
    @CircuitBreaker(
            name = "tripValidation",
            fallbackMethod = "tripValidationFallback"
    )
    public TripDTO validateAndGetTrip(long id){
        TripDTO tripDTO;
        try{
             tripDTO = tripClient.getTrip(id);
        } catch (FeignException.NotFound e) {
            throw new NotFoundException("Trip wasn't found");
        }
        return tripDTO;
    }

    public void tripValidationFallback(long id, Exception ex){
        throw new RuntimeException("Trip service isn't available now");
    }
}
