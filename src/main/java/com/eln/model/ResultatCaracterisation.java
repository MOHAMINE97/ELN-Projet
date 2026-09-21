package com.eln.model;

import com.eln.model.Enums.TypeMesure;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "resultats_caracterisation")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResultatCaracterisation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TypeMesure type;

    @Column(nullable = false)
    private LocalDateTime dateMesure;

    // Valeur numérique principale (ex: épaisseur en nm, résistivité en ohm.cm)
    private Double valeur;

    private String unite; // "nm", "ohm.cm", "%", etc.

    // Chemin ou référence vers le fichier brut uploadé (CSV/TXT), stocké sur disque ou S3
    private String cheminFichierBrut;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "echantillon_id", nullable = false)
    private Echantillon echantillon;
}
