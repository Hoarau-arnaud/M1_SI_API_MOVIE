package com.services.impl;

import com.entities.Genre;
import com.repositories.GenreRepository;
import com.services.GenreService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class GenreServiceImpl implements GenreService {

    private final GenreRepository genreRepository;

    public GenreServiceImpl(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    @Override
    public List<Genre> getAll() {
        return genreRepository.findAll();
    }

    @Override
    public Genre getById(Long id) {
        return genreRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Genre introuvable: id=" + id));
    }

    @Override
    @Transactional
    public Genre create(String name) {
        if (name == null || name.isBlank()) {
            throw new RuntimeException("Le nom du genre est obligatoire");
        }

        return genreRepository.findByNameIgnoreCase(name.trim())
                .orElseGet(() -> {
                    Genre g = new Genre();
                    g.setName(name.trim());
                    return genreRepository.save(g);
                });
    }

    @Override
    @Transactional
    public Boolean delete(Long id) {
        if (!genreRepository.existsById(id)) return false;
        genreRepository.deleteById(id);
        return true;
    }
}