package by.fk8ght.task.car.model.mapper;

import by.fk8ght.task.car.model.dto.CarDTO;
import by.fk8ght.task.car.model.entity.Car;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CarMapper {
    @Mapping(target = "driverId", source = "driver.id")
    CarDTO toDto(Car car);

    @Mapping(target = "driver", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    Car toEntity(CarDTO dto);
}
