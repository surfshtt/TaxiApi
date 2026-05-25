package by.fk8ght.task.rating.service.impl;

import by.fk8ght.task.exception.BadRequestException;
import by.fk8ght.task.exception.ConflictException;
import by.fk8ght.task.exception.NotFoundException;
import by.fk8ght.task.rating.model.dto.CreateRatingDTO;
import by.fk8ght.task.rating.model.dto.RatingDTO;
import by.fk8ght.task.rating.model.entity.RatedBy;
import by.fk8ght.task.rating.model.entity.Rating;
import by.fk8ght.task.rating.model.mapper.RatingMapper;
import by.fk8ght.task.rating.repository.RatingRepository;
import by.fk8ght.task.rating.service.RatingService;
import by.fk8ght.task.trip.model.entity.Trip;
import by.fk8ght.task.trip.model.entity.TripStatus;
import by.fk8ght.task.trip.repository.TripRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class RatingServiceImpl implements RatingService {
    private final RatingRepository repository;
    private final TripRepository tripRepository;
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
        if (!tripRepository.existsById(tripId)) {
            throw new NotFoundException("Trip wasn't found");
        }
        return repository.findAllByTripId(tripId).stream().map(mapper::toDto).toList();
    }

    private RatingDTO saveRating(long tripId, RatedBy ratedBy, CreateRatingDTO dto) {
        Trip trip = tripRepository.findById(tripId).orElseThrow(() -> new NotFoundException("Trip wasn't found"));

        if (trip.getStatus() != TripStatus.COMPLETED) {
            throw new BadRequestException("Trip must be completed before rating");
        }

        if (repository.existsByTripIdAndRatedBy(tripId, ratedBy)) {
            throw new ConflictException("Trip is already rated");
        }

        Rating rating = new Rating();
        rating.setTripId(tripId);
        rating.setDriverId(trip.getDriverId());
        rating.setPassengerId(trip.getPassengerId());
        rating.setRatedBy(ratedBy);
        rating.setScore(dto.getScore());
        rating.setComment(dto.getComment());

        return mapper.toDto(repository.save(rating));
    }
}
