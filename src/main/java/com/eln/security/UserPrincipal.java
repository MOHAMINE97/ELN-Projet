package com.eln.security;

import com.eln.model.Enums.Role;
import com.eln.model.Utilisateur;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * Adapte l'entité Utilisateur (JPA) à l'interface UserDetails attendue par Spring Security,
 * sans mélanger le modèle de persistance et la logique d'authentification.
 */
@Getter
public class UserPrincipal implements UserDetails {

    private final Long id;
    private final String email;
    private final String motDePasseHash;
    private final String nom;
    private final Role role;
    private final Collection<? extends GrantedAuthority> authorities;

    public UserPrincipal(Utilisateur utilisateur) {
        this.id = utilisateur.getId();
        this.email = utilisateur.getEmail();
        this.motDePasseHash = utilisateur.getMotDePasseHash();
        this.nom = utilisateur.getNom();
        // Spring Security attend le préfixe "ROLE_" pour les vérifications hasRole("X")
        this.authorities = List.of(new SimpleGrantedAuthority("ROLE_" + utilisateur.getRole().name()));
        this.role = utilisateur.getRole();
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return motDePasseHash;
    }

    @Override
    public String getUsername() {
        return email; // l'email sert d'identifiant de connexion
    }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return true; }
}

