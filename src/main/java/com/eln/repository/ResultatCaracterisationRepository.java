package com.eln.repository;

import com.eln.model.ResultatCaracterisation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ResultatCaracterisationRepository extends JpaRepository<ResultatCaracterisation, Long> {
    List<ResultatCaracterisation> findAllByEchantillonIdOrderByDateMesureAsc(Long echantillonId);
}
