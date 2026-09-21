package com.eln.model.Enums;

/**
 * Rôles applicatifs. TECHNICIEN peut créer/modifier ses propres échantillons.
 * CHERCHEUR peut consulter et commenter tous les échantillons de son équipe.
 * ADMIN a tous les droits (gestion des utilisateurs, suppression).
 */
public enum Role {
    TECHNICIEN,
    CHERCHEUR,
    ADMIN
}
