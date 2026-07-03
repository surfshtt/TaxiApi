package by.fk8ght.task.trip.model.mapper;

import by.fk8ght.task.trip.model.dto.TripDTO;
import by.fk8ght.task.trip.model.entity.Trip;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TripMapper {
    TripDTO toDto(Trip trip);
    Trip toEntity(TripDTO dto);
}
