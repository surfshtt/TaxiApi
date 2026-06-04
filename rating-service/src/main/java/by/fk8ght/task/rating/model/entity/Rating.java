package by.fk8ght.task.rating.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@Entity
@Table(name = "ratings")
public class Rating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "trip_id", nullable = false)
    private long tripId;

    @Column(name = "driver_id", nullable = false)
    private long driverId;

    @Column(name = "passenger_id", nullable = false)
    private long passengerId;

    @Enumerated(EnumType.STRING)
    @Column(name = "rated_by", nullable = false)
    private RatedBy ratedBy;

    @Column(nullable = false)
    private int score;

    @Column
    private String comment;
}
