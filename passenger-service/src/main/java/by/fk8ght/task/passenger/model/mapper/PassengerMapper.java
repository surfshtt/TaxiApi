package by.fk8ght.task.passenger.model.mapper;

import by.fk8ght.task.passenger.model.dto.PassengerDTO;
import by.fk8ght.task.passenger.model.entity.Passenger;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PassengerMapper {
    PassengerDTO toDto(Passenger passenger);
    Passenger toEntity(PassengerDTO dto);
}
