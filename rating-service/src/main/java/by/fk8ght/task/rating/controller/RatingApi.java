package by.fk8ght.task.rating.controller;

import by.fk8ght.task.rating.model.dto.CreateRatingDTO;
import by.fk8ght.task.rating.model.dto.RatingDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("api/rating")
public interface RatingApi {
    @PostMapping("/trip/{tripId}/passenger")
    ResponseEntity<RatingDTO> rateByPassenger(@PathVariable long tripId, @RequestBody CreateRatingDTO rating);

    @PostMapping("/trip/{tripId}/driver")
    ResponseEntity<RatingDTO> rateByDriver(@PathVariable long tripId, @RequestBody CreateRatingDTO rating);

    @GetMapping("/trip/{tripId}")
    ResponseEntity<List<RatingDTO>> getTripRatings(@PathVariable long tripId);
}
