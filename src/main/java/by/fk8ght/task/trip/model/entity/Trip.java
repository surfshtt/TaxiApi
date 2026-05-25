package by.fk8ght.task.trip.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@Entity
@Table(name = "trips")
public class Trip {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "driver_id", nullable = false)
    private long driverId;

    @Column(name = "passenger_id", nullable = false)
    private long passengerId;

    @Column(name = "pickup_address", nullable = false)
    private String pickupAddress;

    @Column(name = "destination_address", nullable = false)
    private String destinationAddress;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TripStatus status;

    @Column(name = "ordered_at", nullable = false)
    private LocalDateTime orderedAt;

    @Column(nullable = false)
    private BigDecimal cost;
}
