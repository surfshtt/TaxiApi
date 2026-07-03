package by.fk8ght.task.rating.controller.impl;

import by.fk8ght.task.rating.model.dto.CreateRatingDTO;
import by.fk8ght.task.rating.model.dto.RatingDTO;
import by.fk8ght.task.rating.model.entity.RatedBy;
import by.fk8ght.task.rating.service.RatingService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RatingControllerTest {

    @Mock
    private RatingService ratingService;

    @InjectMocks
    private RatingController controller;

    @Test
    void rateByPassengerTest() {
        CreateRatingDTO dto = new CreateRatingDTO();
        dto.setScore(5);
        dto.setComment("Great trip");

        RatingDTO resultDto = new RatingDTO();
        resultDto.setId(100L);
        resultDto.setTripId(1L);
        resultDto.setRatedBy(RatedBy.PASSENGER);
        resultDto.setScore(5);

        when(ratingService.rateByPassenger(eq(1L), any(CreateRatingDTO.class))).thenReturn(resultDto);

        ResponseEntity<RatingDTO> response = controller.rateByPassenger(1L, dto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(100L, response.getBody().getId());
        assertEquals(RatedBy.PASSENGER, response.getBody().getRatedBy());
        verify(ratingService).rateByPassenger(eq(1L), any(CreateRatingDTO.class));
    }

    @Test
    void rateByDriverTest() {
        CreateRatingDTO dto = new CreateRatingDTO();
        dto.setScore(4);
        dto.setComment("Good passenger");

        RatingDTO resultDto = new RatingDTO();
        resultDto.setId(100L);
        resultDto.setTripId(1L);
        resultDto.setRatedBy(RatedBy.DRIVER);
        resultDto.setScore(4);

        when(ratingService.rateByDriver(eq(1L), any(CreateRatingDTO.class))).thenReturn(resultDto);

        ResponseEntity<RatingDTO> response = controller.rateByDriver(1L, dto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(RatedBy.DRIVER, response.getBody().getRatedBy());
        verify(ratingService).rateByDriver(eq(1L), any(CreateRatingDTO.class));
    }

    @Test
    void getTripRatingsTest() {
        RatingDTO dto = new RatingDTO();
        dto.setId(100L);
        dto.setScore(5);

        when(ratingService.getTripRatings(1L)).thenReturn(List.of(dto));

        ResponseEntity<List<RatingDTO>> response = controller.getTripRatings(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals(100L, response.getBody().get(0).getId());
        verify(ratingService).getTripRatings(1L);
    }
}
