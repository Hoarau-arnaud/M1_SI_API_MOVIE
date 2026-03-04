package com.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.Set;

@Data
public class MovieDto {
    private Long id;

    @NotBlank
    private String titre;

    private Integer annee;
    private Integer ageMinimum;

    private Boolean ouvertLocation;
    private Double prixLocation;

    private Long realisateurId;
    private Set<Long> acteurIds;

    private Set<String> genres;
}