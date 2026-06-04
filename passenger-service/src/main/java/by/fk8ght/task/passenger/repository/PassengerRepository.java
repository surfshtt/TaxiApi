package by.fk8ght.task.passenger.repository;

import by.fk8ght.task.passenger.model.entity.Passenger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PassengerRepository extends JpaRepository<Passenger, Long> {
    @Query("""
        select p
        from Passenger p
        where p.deleted = false
    """)
    Page<Passenger> findAllNotDeleted(Pageable pageable);

    @Query("""
        select count(p) > 0
        from Passenger p
        where lower(p.email) = lower(:email)
    """)
    boolean existsByEmail(String email);
}
