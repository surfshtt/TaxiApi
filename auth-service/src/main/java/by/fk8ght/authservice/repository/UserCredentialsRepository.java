package by.fk8ght.authservice.repository;

import by.fk8ght.authservice.model.entity.UserCredentialsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserCredentialsRepository extends JpaRepository<UserCredentialsEntity, Long> {

    Optional<UserCredentialsEntity> findByLogin(String login);

    boolean existsByLogin(String login);

    boolean existsByUserId(Long userId);
}
