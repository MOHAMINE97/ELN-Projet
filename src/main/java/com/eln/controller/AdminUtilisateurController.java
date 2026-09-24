package com.eln.controller;

import com.eln.exception.ResourceNotFoundException;
import com.eln.model.Enums.Role;
import com.eln.model.Utilisateur;
import com.eln.repository.UtilisateurRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/utilisateurs")
@PreAuthorize("hasRole('ADMIN')")
public class AdminUtilisateurController {

    private final UtilisateurRepository utilisateurRepository;

    public AdminUtilisateurController(UtilisateurRepository utilisateurRepository) {
        this.utilisateurRepository = utilisateurRepository;
    }

    @Getter
    @Setter
    public static class ChangerRoleRequest {
        @NotNull
        private Role role;
    }

    @PatchMapping("/{id}/role")
    @Transactional
    public ResponseEntity<Void> changerRole(@PathVariable Long id,
                                            @Valid @RequestBody ChangerRoleRequest request) {
        Utilisateur utilisateur = utilisateurRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur introuvable : id=" + id));
        utilisateur.setRole(request.getRole());
        return ResponseEntity.noContent().build();
    }
}
