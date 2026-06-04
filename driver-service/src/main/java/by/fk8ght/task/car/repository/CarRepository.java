package by.fk8ght.task.car.repository;

import by.fk8ght.task.car.model.entity.Car;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {
    @Query("""
        select c
        from Car c
        where c.deleted = false
    """)
    Page<Car> findAllNotDeleted(Pageable pageable);

    @Query("""
        select count(c) > 0
        from Car c
        where lower(c.plateNumber) = lower(:plateNumber)
    """)
    boolean existsByPlateNumber(String plateNumber);
}
