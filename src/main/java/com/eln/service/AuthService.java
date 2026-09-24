package com.eln.service;

import com.eln.dto.AuthDTO;
import com.eln.model.Enums.Role;
import com.eln.model.Utilisateur;
import com.eln.repository.UtilisateurRepository;
import com.eln.security.JwtService;
import com.eln.security.UserPrincipal;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final UtilisateurRepository utilisateurRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthService(UtilisateurRepository utilisateurRepository,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService,
                       AuthenticationManager authenticationManager) {
        this.utilisateurRepository = utilisateurRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    @Transactional
    public AuthDTO.AuthResponse inscrire(AuthDTO.RegisterRequest request) {
        if (utilisateurRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Un compte existe déjà avec cet email.");
        }

        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setEmail(request.getEmail());
        utilisateur.setMotDePasseHash(passwordEncoder.encode(request.getMotDePasse()));
        utilisateur.setNom(request.getNom());
        // Tout nouveau compte est TECHNICIEN ; seul un ADMIN peut changer un rôle (AdminUtilisateurController)
        utilisateur.setRole(Role.TECHNICIEN);

        Utilisateur sauvegarde = utilisateurRepository.save(utilisateur);
        UserPrincipal principal = new UserPrincipal(sauvegarde);
        String token = jwtService.genererToken(principal);

        return new AuthDTO.AuthResponse(token, sauvegarde.getId(), sauvegarde.getEmail(),
                sauvegarde.getNom(), sauvegarde.getRole());
    }

    public AuthDTO.AuthResponse connecter(AuthDTO.LoginRequest request) {
        // Délègue à Spring Security la vérification email/mot de passe (via CustomUserDetailsService
        // + PasswordEncoder) ; lève une BadCredentialsException si incorrect, gérée globalement.
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getMotDePasse()));

        Utilisateur utilisateur = utilisateurRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalStateException("Utilisateur introuvable après authentification."));

        UserPrincipal principal = new UserPrincipal(utilisateur);
        String token = jwtService.genererToken(principal);

        return new AuthDTO.AuthResponse(token, utilisateur.getId(), utilisateur.getEmail(),
                utilisateur.getNom(), utilisateur.getRole());
    }
}
