package com.sondages.repository;

import com.sondages.model.Commentaire;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentaireRepository extends JpaRepository<Commentaire, Long> {
    Optional<Commentaire> findById(Long id);
}