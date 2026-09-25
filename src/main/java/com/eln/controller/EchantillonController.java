package com.eln.controller;

import com.eln.dto.EchantillonDTO;
import com.eln.dto.EtapeProcedeDTO;
import com.eln.dto.ResultatCaracterisationDTO;
import com.eln.security.UserPrincipal;
import com.eln.service.EchantillonService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

    @PostMapping
    public ResponseEntity<EchantillonDTO.DetailResponse> creer(
            @Valid @RequestBody EchantillonDTO.CreateRequest request,
            @AuthenticationPrincipal UserPrincipal utilisateur) {
        EchantillonDTO.DetailResponse cree = echantillonService.creer(request, utilisateur.getId());
        return ResponseEntity.created(URI.create("/api/echantillons/" + cree.getId())).body(cree);
    }

    @GetMapping
    public ResponseEntity<List<EchantillonDTO.SummaryResponse>> listerTous() {
        return ResponseEntity.ok(echantillonService.listerTous());
    }

    @GetMapping("/mine")
    public ResponseEntity<List<EchantillonDTO.SummaryResponse>> mesEchantillons(
            @AuthenticationPrincipal UserPrincipal utilisateur) {
        return ResponseEntity.ok(echantillonService.listerParProprietaire(utilisateur.getId()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EchantillonDTO.DetailResponse> obtenirDetail(
            @PathVariable Long id, @AuthenticationPrincipal UserPrincipal utilisateur) {
        return ResponseEntity.ok(echantillonService.obtenirDetail(id,utilisateur));
    }

    @PostMapping("/{id}/etapes")
    public ResponseEntity<EtapeProcedeDTO.Response> ajouterEtape(
            @PathVariable Long id,
            @Valid @RequestBody EtapeProcedeDTO.Request request,
            @AuthenticationPrincipal UserPrincipal utilisateur) {
        return ResponseEntity.status(HttpStatus.CREATED).body(echantillonService.ajouterEtape(id, request,utilisateur));
    }

    @PostMapping("/{id}/resultats")
    public ResponseEntity<ResultatCaracterisationDTO.Response> ajouterResultat(
            @PathVariable Long id,
            @Valid @RequestBody ResultatCaracterisationDTO.Request request,
            @AuthenticationPrincipal UserPrincipal utilisateur) {
        return ResponseEntity.status(HttpStatus.CREATED).body(echantillonService.ajouterResultat(id, request,utilisateur));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimer(@PathVariable Long id) {
        echantillonService.supprimer(id);
        return ResponseEntity.noContent().build();
    }
}
