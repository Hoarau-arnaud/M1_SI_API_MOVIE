package com.services.impl;

import com.dtos.MovieDto;
import com.entities.Genre;
import com.entities.Movie;
import com.mappers.MovieMapper;
import com.repositories.GenreRepository;
import com.repositories.MovieRepository;
import com.services.MovieService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Service
public class MovieServiceImpl implements MovieService {

    private final MovieRepository movieRepository;
    private final GenreRepository genreRepository;
    private final MovieMapper movieMapper;

    public MovieServiceImpl(MovieRepository movieRepository, GenreRepository genreRepository, MovieMapper movieMapper) {
        this.movieRepository = movieRepository;
        this.genreRepository = genreRepository;
        this.movieMapper = movieMapper;
    }

    @Override
    public List<MovieDto> getAllMovies() {
        return movieRepository.findAll().stream()
                .map(movieMapper::toDto)
                .toList();
    }

    @Override
    public MovieDto getMovieById(Long id) {
        Movie m = movieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Film introuvable: id=" + id));
        return movieMapper.toDto(m);
    }

    @Override
    @Transactional
    public MovieDto createMovie(MovieDto dto) {
        dto.setId(null);

        movieRepository.findByTitre(dto.getTitre()).ifPresent(x -> {
            throw new RuntimeException("Titre déjà utilisé: " + dto.getTitre());
        });

        Movie m = movieMapper.toEntity(dto);

        // Par défaut: fermé à la location
        m.setOuvertLocation(false);
        m.setPrixLocation(null);

        // genres (DB)
        syncGenres(m, dto.getGenres());

        Movie saved = movieRepository.save(m);
        return movieMapper.toDto(saved);
    }

    @Override
    @Transactional
    public MovieDto updateMovie(Long id, MovieDto dto) {
        Movie existing = movieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Film introuvable: id=" + id));

        if (dto.getTitre() != null && !dto.getTitre().equals(existing.getTitre())) {
            movieRepository.findByTitre(dto.getTitre()).ifPresent(x -> {
                throw new RuntimeException("Titre déjà utilisé: " + dto.getTitre());
            });
        }

        movieMapper.updateEntity(existing, dto);

        // genres (DB)
        syncGenres(existing, dto.getGenres());

        Movie saved = movieRepository.save(existing);
        return movieMapper.toDto(saved);
    }

    @Override
    @Transactional
    public MovieDto openMovie(Long id, Double prix) {
        if (prix == null || prix <= 0) {
            throw new RuntimeException("Le prix doit être > 0");
        }

        Movie m = movieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Film introuvable: id=" + id));

        m.setOuvertLocation(true);
        m.setPrixLocation(prix);

        return movieMapper.toDto(movieRepository.save(m));
    }

    @Override
    @Transactional
    public MovieDto closeMovie(Long id) {
        Movie m = movieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Film introuvable: id=" + id));

        m.setOuvertLocation(false);
        m.setPrixLocation(null);

        return movieMapper.toDto(movieRepository.save(m));
    }

    private void syncGenres(Movie movie, Set<String> genreNames) {
        movie.getGenres().clear();

        if (genreNames == null) return;

        Set<Genre> resolved = new LinkedHashSet<>();
        for (String raw : genreNames) {
            if (raw == null) continue;
            String name = raw.trim();
            if (name.isBlank()) continue;

            Genre g = genreRepository.findByNameIgnoreCase(name)
                    .orElseGet(() -> {
                        Genre created = new Genre();
                        created.setName(name);
                        return genreRepository.save(created);
                    });

            resolved.add(g);
        }

        movie.getGenres().addAll(resolved);
    }
}