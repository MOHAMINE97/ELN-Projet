package com.eln.model;

import com.eln.model.Enums.TypeEtape;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "etapes_procede")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EtapeProcede {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TypeEtape type;

    @Column(nullable = false)
    private LocalDateTime dateExecution;

    // Paramètres les plus courants en colonnes dédiées (faciles à filtrer/requêter)
    private Double temperatureCelsius;
    private Double dureeMinutes;
    private Double pressionMbar;
    private Double puissancePlasmaWatts;

    // Paramètres additionnels moins standards : stockés en JSON brut (String ici pour rester simple ;
    // évoluable plus tard vers un type JSONB natif avec la lib hibernate-types)
    @Column(columnDefinition = "TEXT")
    private String parametresComplementairesJson;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "echantillon_id", nullable = false)
    private Echantillon echantillon;
}
