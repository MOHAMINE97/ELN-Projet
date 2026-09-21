package com.eln.dto;

import com.eln.model.Enums.TypeEtape;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

public class EtapeProcedeDTO {

    @Getter
    @Setter
    public static class Request {
        @NotNull
        private TypeEtape type;

        @NotNull
        private LocalDateTime dateExecution;

        private Double temperatureCelsius;
        private Double dureeMinutes;
        private Double pressionMbar;
        private Double puissancePlasmaWatts;
        private String parametresComplementairesJson;
        private String notes;
    }

    @Getter
    @Setter
    public static class Response {
        private Long id;
        private TypeEtape type;
        private LocalDateTime dateExecution;
        private Double temperatureCelsius;
        private Double dureeMinutes;
        private Double pressionMbar;
        private Double puissancePlasmaWatts;
        private String parametresComplementairesJson;
        private String notes;
    }
}
