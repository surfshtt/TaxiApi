package by.fk8ght.task.driver.repository;

import by.fk8ght.task.driver.model.entity.Driver;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DriverRepository extends JpaRepository<Driver, Long> {
    @Query("""
        select d
        from Driver d
        where d.deleted = false
    """)
    Page<Driver> findAllNotDeleted(Pageable pageable);

    @Query("""
        select count(d) > 0
        from Driver d
        where lower(d.email) = lower(:email)
    """)
    boolean existsByEmail(String email);
}
