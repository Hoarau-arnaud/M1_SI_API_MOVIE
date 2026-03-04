package com.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "movie")
@Data
@NoArgsConstructor
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String titre;

    private Integer annee;

    private Integer ageMinimum;

    private Boolean ouvertLocation = false;

    private Double prixLocation;

    // IDs venant de artist-api (pas de relation JPA ici)
    private Long realisateurId;

    // Simple pour TP : "2,5,9"
    @Column(length = 1000)
    private String acteurIds;

    // ex: "Action,Thriller"
    @ManyToMany
    @JoinTable(
            name = "movie_genre",
            joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    private java.util.Set<Genre> genres = new java.util.HashSet<>();
}