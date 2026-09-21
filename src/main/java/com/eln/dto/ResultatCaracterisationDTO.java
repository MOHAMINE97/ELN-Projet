package com.eln.dto;

import com.eln.model.Enums.TypeMesure;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

public class ResultatCaracterisationDTO {
    @Getter
    @Setter
    public static class Request {
        @NotNull
        private TypeMesure type;

        @NotNull
        private LocalDateTime dateMesure;

        private Double valeur;
        private String unite;
        private String cheminFichierBrut;
        private String notes;
    }

    @Getter
    @Setter
    public static class Response {
        private Long id;
        private TypeMesure type;
        private LocalDateTime dateMesure;
        private Double valeur;
        private String unite;
        private String cheminFichierBrut;
        private String notes;
    }
}
