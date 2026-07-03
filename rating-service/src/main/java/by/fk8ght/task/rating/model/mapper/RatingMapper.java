package by.fk8ght.task.rating.model.mapper;

import by.fk8ght.task.rating.model.dto.RatingDTO;
import by.fk8ght.task.rating.model.entity.Rating;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RatingMapper {
    RatingDTO toDto(Rating rating);
}
