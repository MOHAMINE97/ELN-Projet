package com.eln.repository;

import com.eln.model.Echantillon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.*;

public interface EchantillonRepository extends JpaRepository<Echantillon, Long> {
    Optional<Echantillon> findByReference(String reference);
    boolean existsByReference(String reference);

    // Un TECHNICIEN ne voit que ses propres échantillons
    List<Echantillon> findByProprietaireId(Long proprietaireId);

    // Recherche simple pour la barre de filtre du front
    List<Echantillon> findByMateriauContainingIgnoreCase(String materiau);
}
