package com.eln.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "echantillon")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Echantillon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Référence lisible pour le labo, ex: "SIC-2026-014"
    @Column(nullable = false, unique = true)
    private String reference;

    @Column(nullable = false)
    private String materiau; // ex: "SiC", "cristal liquide 5CB", "Si/SiO2"

    private String description;

    @Column(nullable = false)
    private LocalDateTime dateCreation = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "proprietaire_id", nullable = false)
    private Utilisateur proprietaire;

    // Historique des étapes de fabrication, dans l'ordre chronologique
    @OneToMany(mappedBy = "echantillon", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("dateExecution ASC")
    private List<EtapeProcede> etapes = new ArrayList<>();

    // Résultats de caractérisation associés
    @OneToMany(mappedBy = "echantillon", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("dateMesure ASC")
    private List<ResultatCaracterisation> resultats = new ArrayList<>();
}
