package dev.mellak.pigeonal.repository;

import dev.mellak.pigeonal.domain.Pigeon;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Pigeon entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PigeonRepository extends JpaRepository<Pigeon, Long> {}
