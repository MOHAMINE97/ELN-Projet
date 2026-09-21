package com.eln.repository;

import com.eln.model.EtapeProcede;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EtapeProcedeRepository extends JpaRepository<EtapeProcede, Long> {
    List<EtapeProcede> findByEchantillonIdOrderByDateExecutionAsc(Long echantillonId);
}
