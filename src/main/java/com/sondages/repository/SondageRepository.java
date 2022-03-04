package com.sondages.repository;

import com.sondages.model.Sondage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SondageRepository extends JpaRepository<Sondage, Long> {
    Optional<Sondage> findById(Long id);
}
