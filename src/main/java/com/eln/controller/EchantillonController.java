package com.eln.controller;

import com.eln.dto.EchantillonDTO;
import com.eln.dto.EtapeProcedeDTO;
import com.eln.dto.ResultatCaracterisationDTO;
import com.eln.service.EchantillonService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/echantillons")
public class EchantillonController {

    private final EchantillonService echantillonService;

    public EchantillonController(EchantillonService echantillonService) {
        this.echantillonService = echantillonService;
    }

    // NB : proprietaireId viendra plus tard du token JWT (utilisateur connecté)
    // plutôt que d'un paramètre de requête — simplification temporaire pour cette étape.
    @PostMapping
    public ResponseEntity<EchantillonDTO.DetailResponse> creer(
            @Valid @RequestBody EchantillonDTO.CreateRequest request,
            @RequestParam Long proprietaireId) {
        EchantillonDTO.DetailResponse cree = echantillonService.creer(request, proprietaireId);
        return ResponseEntity.created(URI.create("/api/echantillons/" + cree.getId())).body(cree);
    }

    @GetMapping
    public ResponseEntity<List<EchantillonDTO.SummaryResponse>> listerTous(
            @RequestParam(required = false) Long proprietaireId) {
        List<EchantillonDTO.SummaryResponse> resultats = (proprietaireId != null)
                ? echantillonService.listerParProprietaire(proprietaireId)
                : echantillonService.listerTous();
        return ResponseEntity.ok(resultats);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EchantillonDTO.DetailResponse> obtenirDetail(@PathVariable Long id) {
        return ResponseEntity.ok(echantillonService.obtenirDetail(id));
    }

    @PostMapping("/{id}/etapes")
    public ResponseEntity<EtapeProcedeDTO.Response> ajouterEtape(
            @PathVariable Long id,
            @Valid @RequestBody EtapeProcedeDTO.Request request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(echantillonService.ajouterEtape(id, request));
    }

    @PostMapping("/{id}/resultats")
    public ResponseEntity<ResultatCaracterisationDTO.Response> ajouterResultat(
            @PathVariable Long id,
            @Valid @RequestBody ResultatCaracterisationDTO.Request request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(echantillonService.ajouterResultat(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimer(@PathVariable Long id) {
        echantillonService.supprimer(id);
        return ResponseEntity.noContent().build();
    }
}
