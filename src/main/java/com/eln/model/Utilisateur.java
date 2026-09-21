package com.eln.model;


import com.eln.model.Enums.Role;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "utilisateurs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Utilisateur {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String motDePasseHash; // toujours stocker un hash (BCrypt), jamais le mot de passe en clair

    @Column(nullable = false)
    private String nom;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column(nullable = false)
    private LocalDateTime dateCreation = LocalDateTime.now();

    // Un utilisateur peut être propriétaire de plusieurs échantillons
    @OneToMany(mappedBy = "proprietaire", cascade = CascadeType.ALL, orphanRemoval = false)
    private List<Echantillon> echantillons = new ArrayList<>();
}
