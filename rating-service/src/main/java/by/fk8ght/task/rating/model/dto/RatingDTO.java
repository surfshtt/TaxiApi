package by.fk8ght.task.rating.model.dto;

import by.fk8ght.task.rating.model.entity.RatedBy;
import lombok.Data;

@Data
public class RatingDTO {
    private Long id;
    private Long tripId;
    private Long driverId;
    private Long passengerId;
    private RatedBy ratedBy;
    private int score;
    private String comment;
}
