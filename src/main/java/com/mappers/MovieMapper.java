package com.mappers;

import com.dtos.MovieDto;
import com.entities.Genre;
import com.entities.Movie;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class MovieMapper {

    public MovieDto toDto(Movie m) {
        if (m == null) return null;

        MovieDto dto = new MovieDto();
        dto.setId(m.getId());
        dto.setTitre(m.getTitre());
        dto.setAnnee(m.getAnnee());
        dto.setAgeMinimum(m.getAgeMinimum());
        dto.setOuvertLocation(m.getOuvertLocation());
        dto.setPrixLocation(m.getPrixLocation());
        dto.setRealisateurId(m.getRealisateurId());
        dto.setActeurIds(parseCsvToSet(m.getActeurIds()));

        // Set<Genre> -> Set<String>
        dto.setGenres(
                m.getGenres().stream()
                        .map(Genre::getName)
                        .collect(Collectors.toCollection(LinkedHashSet::new))
        );

        return dto;
    }

    /**
     * Création d'entité sans mapper les genres (car il faut DB pour résoudre/créer Genre)
     * Les genres seront gérés dans le Service.
     */
    public Movie toEntity(MovieDto dto) {
        if (dto == null) return null;

        Movie m = new Movie();
        if (dto.getId() != null) m.setId(dto.getId());

        m.setTitre(dto.getTitre());
        m.setAnnee(dto.getAnnee());
        m.setAgeMinimum(dto.getAgeMinimum());
        m.setOuvertLocation(dto.getOuvertLocation());
        m.setPrixLocation(dto.getPrixLocation());
        m.setRealisateurId(dto.getRealisateurId());
        m.setActeurIds(joinSetToCsv(dto.getActeurIds()));

        // genres gérés dans le service
        return m;
    }

    public void updateEntity(Movie target, MovieDto dto) {
        target.setTitre(dto.getTitre());
        target.setAnnee(dto.getAnnee());
        target.setAgeMinimum(dto.getAgeMinimum());
        target.setRealisateurId(dto.getRealisateurId());
        target.setActeurIds(joinSetToCsv(dto.getActeurIds()));
        // genres + open/close gérés dans le service
    }

    private Set<Long> parseCsvToSet(String csv) {
        if (csv == null || csv.isBlank()) return new LinkedHashSet<>();
        return Arrays.stream(csv.split(","))
                .map(String::trim)
                .filter(s -> !s.isBlank())
                .map(Long::valueOf)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    private String joinSetToCsv(Set<Long> ids) {
        if (ids == null || ids.isEmpty()) return null;
        return ids.stream()
                .filter(x -> x != null)
                .map(String::valueOf)
                .collect(Collectors.joining(","));
    }
}