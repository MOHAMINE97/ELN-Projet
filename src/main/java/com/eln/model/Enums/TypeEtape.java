package com.eln.model.Enums;

/**
 * Types d'étapes de procédé de fabrication d'un échantillon.
 * Extensible facilement (NETTOYAGE, RECUIT, DOPAGE, etc.)
 */
public enum TypeEtape {
    DEPOT_PVD,
    DEPOT_CVD,
    LITHOGRAPHIE,
    GRAVURE_PLASMA,
    RECUIT,
    AUTRE
}
