package by.fk8ght.task.driver.model.mapper;

import by.fk8ght.task.driver.model.dto.DriverDTO;
import by.fk8ght.task.driver.model.entity.Driver;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DriverMapper {
    DriverDTO toDto(Driver driver);
    Driver toEntity(DriverDTO dto);
}
