package com.eln.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

public class EchantillonDTO {
    // Ce que le client envoie pour créer un échantillon
    @Getter
    @Setter
    public static class CreateRequest {
        @NotBlank
        private String reference;

        @NotBlank
        private String materiau;

        private String description;
    }

    // Ce que l'API renvoie : résumé (liste)
    @Getter
    @Setter
    public static class SummaryResponse {
        private Long id;
        private String reference;
        private String materiau;
        private LocalDateTime dateCreation;
        private String proprietaireNom;
    }

    // Ce que l'API renvoie : détail complet avec historique
    @Getter
    @Setter
    public static class DetailResponse {
        private Long id;
        private String reference;
        private String materiau;
        private String description;
        private LocalDateTime dateCreation;
        private String proprietaireNom;
        private List<EtapeProcedeDTO.Response> etapes;
        private List<ResultatCaracterisationDTO.Response> resultats;
    }
}
