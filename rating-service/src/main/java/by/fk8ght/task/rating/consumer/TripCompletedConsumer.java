package by.fk8ght.task.rating.consumer;

import by.fk8ght.task.rating.model.event.TripCompleted;
import by.fk8ght.task.rating.service.RatingService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TripCompletedConsumer {
    private final RatingService service;

    @KafkaListener(
            topics = "trip-completed",
            groupId = "rating-group"
    )
    public void consume(TripCompleted event) {
        service.consumersSignal(event);
    }
}
