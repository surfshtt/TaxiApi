package by.fk8ght.task.rating.controller.impl;

import by.fk8ght.task.rating.controller.RatingApi;
import by.fk8ght.task.rating.model.dto.CreateRatingDTO;
import by.fk8ght.task.rating.model.dto.RatingDTO;
import by.fk8ght.task.rating.service.RatingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class RatingController implements RatingApi {
    private final RatingService ratingService;

    @Override
    public ResponseEntity<RatingDTO> rateByPassenger(long tripId, @Valid CreateRatingDTO rating) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ratingService.rateByPassenger(tripId, rating));
    }

    @Override
    public ResponseEntity<RatingDTO> rateByDriver(long tripId, @Valid CreateRatingDTO rating) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ratingService.rateByDriver(tripId, rating));
    }

    @Override
    public ResponseEntity<List<RatingDTO>> getTripRatings(long tripId) {
        return ResponseEntity.ok(ratingService.getTripRatings(tripId));
    }
}
