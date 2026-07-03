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
import by.fk8ght.task.rating.model.mapper.RatingMapper;
import by.fk8ght.task.rating.repository.RatingRepository;
import feign.FeignException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RatingServiceImplTest {

    @Mock
    private RatingRepository repository;

    @Mock
    private TripClient tripClient;

    @Mock
    private RatingMapper mapper;

    @InjectMocks
    private RatingServiceImpl service;

    @Test
    void rateByPassengerTest() {
        CreateRatingDTO dto = new CreateRatingDTO();
        dto.setScore(5);
        dto.setComment("Great trip");

        TripDTO tripDTO = new TripDTO();
        tripDTO.setId(1L);
        tripDTO.setDriverId(10L);
        tripDTO.setPassengerId(20L);
        tripDTO.setStatus(TripStatus.ACCEPTED);

        Rating rating = new Rating();
        rating.setId(100L);
        rating.setTripId(1L);
        rating.setDriverId(10L);
        rating.setPassengerId(20L);
        rating.setRatedBy(RatedBy.PASSENGER);
        rating.setScore(5);
        rating.setComment("Great trip");

        RatingDTO resultDto = new RatingDTO();
        resultDto.setId(100L);
        resultDto.setTripId(1L);
        resultDto.setDriverId(10L);
        resultDto.setPassengerId(20L);
        resultDto.setRatedBy(RatedBy.PASSENGER);
        resultDto.setScore(5);
        resultDto.setComment("Great trip");

        when(tripClient.getTrip(1L)).thenReturn(tripDTO);
        when(repository.existsByTripIdAndRatedBy(1L, RatedBy.PASSENGER)).thenReturn(false);
        when(repository.save(any(Rating.class))).thenReturn(rating);
        when(mapper.toDto(rating)).thenReturn(resultDto);

        RatingDTO result = service.rateByPassenger(1L, dto);

        assertEquals(100L, result.getId());
        assertEquals(RatedBy.PASSENGER, result.getRatedBy());
        assertEquals(5, result.getScore());
        verify(repository).save(any(Rating.class));
    }

    @Test
    void rateByDriverTest() {
        CreateRatingDTO dto = new CreateRatingDTO();
        dto.setScore(4);
        dto.setComment("Good passenger");

        TripDTO tripDTO = new TripDTO();
        tripDTO.setId(1L);
        tripDTO.setDriverId(10L);
        tripDTO.setPassengerId(20L);
        tripDTO.setStatus(TripStatus.ACCEPTED);

        Rating rating = new Rating();
        rating.setId(100L);
        rating.setTripId(1L);
        rating.setDriverId(10L);
        rating.setPassengerId(20L);
        rating.setRatedBy(RatedBy.DRIVER);
        rating.setScore(4);
        rating.setComment("Good passenger");

        RatingDTO resultDto = new RatingDTO();
        resultDto.setId(100L);
        resultDto.setRatedBy(RatedBy.DRIVER);
        resultDto.setScore(4);

        when(tripClient.getTrip(1L)).thenReturn(tripDTO);
        when(repository.existsByTripIdAndRatedBy(1L, RatedBy.DRIVER)).thenReturn(false);
        when(repository.save(any(Rating.class))).thenReturn(rating);
        when(mapper.toDto(rating)).thenReturn(resultDto);

        RatingDTO result = service.rateByDriver(1L, dto);

        assertEquals(RatedBy.DRIVER, result.getRatedBy());
        assertEquals(4, result.getScore());
        verify(repository).save(any(Rating.class));
    }

    @Test
    void rateTripNotCompletedTest() {
        CreateRatingDTO dto = new CreateRatingDTO();
        dto.setScore(5);

        TripDTO tripDTO = new TripDTO();
        tripDTO.setId(1L);
        tripDTO.setStatus(TripStatus.CREATED);

        when(tripClient.getTrip(1L)).thenReturn(tripDTO);

        assertThrows(BadRequestException.class, () -> service.rateByPassenger(1L, dto));
    }

    @Test
    void rateTripNotFoundTest() {
        CreateRatingDTO dto = new CreateRatingDTO();
        dto.setScore(5);

        FeignException.NotFound notFound = mock(FeignException.NotFound.class);
        when(tripClient.getTrip(1L)).thenThrow(notFound);

        assertThrows(NotFoundException.class, () -> service.rateByPassenger(1L, dto));
    }

    @Test
    void rateAlreadyRatedTest() {
        CreateRatingDTO dto = new CreateRatingDTO();
        dto.setScore(5);

        TripDTO tripDTO = new TripDTO();
        tripDTO.setId(1L);
        tripDTO.setStatus(TripStatus.ACCEPTED);

        when(tripClient.getTrip(1L)).thenReturn(tripDTO);
        when(repository.existsByTripIdAndRatedBy(1L, RatedBy.PASSENGER)).thenReturn(true);

        assertThrows(ConflictException.class, () -> service.rateByPassenger(1L, dto));
    }

    @Test
    void getTripRatingsTest() {
        TripDTO tripDTO = new TripDTO();
        tripDTO.setId(1L);
        tripDTO.setStatus(TripStatus.ACCEPTED);

        Rating rating = new Rating();
        rating.setId(100L);
        rating.setScore(5);

        RatingDTO dto = new RatingDTO();
        dto.setId(100L);
        dto.setScore(5);

        when(tripClient.getTrip(1L)).thenReturn(tripDTO);
        when(repository.findAllByTripId(1L)).thenReturn(List.of(rating));
        when(mapper.toDto(rating)).thenReturn(dto);

        List<RatingDTO> result = service.getTripRatings(1L);

        assertEquals(1, result.size());
        assertEquals(100L, result.get(0).getId());
    }

    @Test
    void getTripRatingsTripNotFoundTest() {
        FeignException.NotFound notFound = mock(FeignException.NotFound.class);
        when(tripClient.getTrip(1L)).thenThrow(notFound);

        assertThrows(NotFoundException.class, () -> service.getTripRatings(1L));
    }

    @Test
    void consumersSignalTest() {
        by.fk8ght.task.rating.model.event.TripCompleted event = 
            new by.fk8ght.task.rating.model.event.TripCompleted(1L, 10L, 20L);

        assertDoesNotThrow(() -> service.consumersSignal(event));
    }

    @Test
    void tripValidationFallbackTest() {
        Exception ex = new RuntimeException("Service unavailable");

        assertThrows(RuntimeException.class, () -> service.tripValidationFallback(1L, ex));
    }
}
