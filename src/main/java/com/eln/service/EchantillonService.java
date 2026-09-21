package com.eln.service;

import com.eln.dto.EchantillonDTO;
import com.eln.dto.EtapeProcedeDTO;
import com.eln.dto.ResultatCaracterisationDTO;
import com.eln.model.Echantillon;
import com.eln.model.EtapeProcede;
import com.eln.model.ResultatCaracterisation;
import com.eln.model.Utilisateur;
import com.eln.repository.EchantillonRepository;
import com.eln.repository.EtapeProcedeRepository;
import com.eln.repository.ResultatCaracterisationRepository;
import com.eln.repository.UtilisateurRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class EchantillonService  {

    private final EchantillonRepository echantillonRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final EtapeProcedeRepository etapeProcedeRepository;
    private final ResultatCaracterisationRepository resultatRepository;

    public EchantillonService(EchantillonRepository echantillonRepository,
                              UtilisateurRepository utilisateurRepository,
                              EtapeProcedeRepository etapeProcedeRepository,
                              ResultatCaracterisationRepository resultatRepository) {
        this.echantillonRepository = echantillonRepository;
        this.utilisateurRepository = utilisateurRepository;
        this.etapeProcedeRepository = etapeProcedeRepository;
        this.resultatRepository = resultatRepository;
    }

    public EchantillonDTO.DetailResponse creer(EchantillonDTO.CreateRequest request, Long proprietaireId) {
        if (echantillonRepository.existsByReference(request.getReference())) {
            throw new IllegalArgumentException("La référence '" + request.getReference() + "' existe déjà.");
        }

        Utilisateur proprietaire = utilisateurRepository.findById(proprietaireId)
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur introuvable : id=" + proprietaireId));

        Echantillon echantillon = new Echantillon();
        echantillon.setReference(request.getReference());
        echantillon.setMateriau(request.getMateriau());
        echantillon.setDescription(request.getDescription());
        echantillon.setProprietaire(proprietaire);

        Echantillon sauvegarde = echantillonRepository.save(echantillon);
        return toDetailResponse(sauvegarde);
    }

    @Transactional(readOnly = true)
    public List<EchantillonDTO.SummaryResponse> listerTous() {
        return echantillonRepository.findAll().stream()
                .map(this::toSummaryResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<EchantillonDTO.SummaryResponse> listerParProprietaire(Long proprietaireId) {
        return echantillonRepository.findByProprietaireId(proprietaireId).stream()
                .map(this::toSummaryResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public EchantillonDTO.DetailResponse obtenirDetail(Long id) {
        Echantillon echantillon = echantillonRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Échantillon introuvable : id=" + id));
        return toDetailResponse(echantillon);
    }

    public EtapeProcedeDTO.Response ajouterEtape(Long echantillonId, EtapeProcedeDTO.Request request) {
        Echantillon echantillon = echantillonRepository.findById(echantillonId)
                .orElseThrow(() -> new IllegalArgumentException("Échantillon introuvable : id=" + echantillonId));

        EtapeProcede etape = new EtapeProcede();
        etape.setType(request.getType());
        etape.setDateExecution(request.getDateExecution());
        etape.setTemperatureCelsius(request.getTemperatureCelsius());
        etape.setDureeMinutes(request.getDureeMinutes());
        etape.setPressionMbar(request.getPressionMbar());
        etape.setPuissancePlasmaWatts(request.getPuissancePlasmaWatts());
        etape.setParametresComplementairesJson(request.getParametresComplementairesJson());
        etape.setNotes(request.getNotes());
        etape.setEchantillon(echantillon);

        EtapeProcede sauvegarde = etapeProcedeRepository.save(etape);
        return toEtapeResponse(sauvegarde);
    }

    public ResultatCaracterisationDTO.Response ajouterResultat(Long echantillonId, ResultatCaracterisationDTO.Request request) {
        Echantillon echantillon = echantillonRepository.findById(echantillonId)
                .orElseThrow(() -> new IllegalArgumentException("Échantillon introuvable : id=" + echantillonId));

        ResultatCaracterisation resultat = new ResultatCaracterisation();
        resultat.setType(request.getType());
        resultat.setDateMesure(request.getDateMesure());
        resultat.setValeur(request.getValeur());
        resultat.setUnite(request.getUnite());
        resultat.setCheminFichierBrut(request.getCheminFichierBrut());
        resultat.setNotes(request.getNotes());
        resultat.setEchantillon(echantillon);

        ResultatCaracterisation sauvegarde = resultatRepository.save(resultat);
        return toResultatResponse(sauvegarde);
    }

    public void supprimer(Long id) {
        if (!echantillonRepository.existsById(id)) {
            throw new IllegalArgumentException("Échantillon introuvable : id=" + id);
        }
        echantillonRepository.deleteById(id);
    }

    // --- Conversions entité -> DTO ---

    private EchantillonDTO.SummaryResponse toSummaryResponse(Echantillon e) {
        EchantillonDTO.SummaryResponse dto = new EchantillonDTO.SummaryResponse();
        dto.setId(e.getId());
        dto.setReference(e.getReference());
        dto.setMateriau(e.getMateriau());
        dto.setDateCreation(e.getDateCreation());
        dto.setProprietaireNom(e.getProprietaire() != null ? e.getProprietaire().getNom() : null);
        return dto;
    }

    private EchantillonDTO.DetailResponse toDetailResponse(Echantillon e) {
        EchantillonDTO.DetailResponse dto = new EchantillonDTO.DetailResponse();
        dto.setId(e.getId());
        dto.setReference(e.getReference());
        dto.setMateriau(e.getMateriau());
        dto.setDescription(e.getDescription());
        dto.setDateCreation(e.getDateCreation());
        dto.setProprietaireNom(e.getProprietaire() != null ? e.getProprietaire().getNom() : null);
        dto.setEtapes(e.getEtapes().stream().map(this::toEtapeResponse).collect(Collectors.toList()));
        dto.setResultats(e.getResultats().stream().map(this::toResultatResponse).collect(Collectors.toList()));
        return dto;
    }

    private EtapeProcedeDTO.Response toEtapeResponse(EtapeProcede etape) {
        EtapeProcedeDTO.Response dto = new EtapeProcedeDTO.Response();
        dto.setId(etape.getId());
        dto.setType(etape.getType());
        dto.setDateExecution(etape.getDateExecution());
        dto.setTemperatureCelsius(etape.getTemperatureCelsius());
        dto.setDureeMinutes(etape.getDureeMinutes());
        dto.setPressionMbar(etape.getPressionMbar());
        dto.setPuissancePlasmaWatts(etape.getPuissancePlasmaWatts());
        dto.setParametresComplementairesJson(etape.getParametresComplementairesJson());
        dto.setNotes(etape.getNotes());
        return dto;
    }

    private ResultatCaracterisationDTO.Response toResultatResponse(ResultatCaracterisation resultat) {
        ResultatCaracterisationDTO.Response dto = new ResultatCaracterisationDTO.Response();
        dto.setId(resultat.getId());
        dto.setType(resultat.getType());
        dto.setDateMesure(resultat.getDateMesure());
        dto.setValeur(resultat.getValeur());
        dto.setUnite(resultat.getUnite());
        dto.setCheminFichierBrut(resultat.getCheminFichierBrut());
        dto.setNotes(resultat.getNotes());
        return dto;
    }
}
