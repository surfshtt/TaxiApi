package by.fk8ght.task.rating.repository;

import by.fk8ght.task.rating.model.entity.RatedBy;
import by.fk8ght.task.rating.model.entity.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {
    boolean existsByTripIdAndRatedBy(long tripId, RatedBy ratedBy);
    List<Rating> findAllByTripId(long tripId);
    Optional<Rating> findByTripIdAndRatedBy(long tripId, RatedBy ratedBy);
}
