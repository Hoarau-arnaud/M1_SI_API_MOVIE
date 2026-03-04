package com.services;

import com.entities.Genre;

import java.util.List;

public interface GenreService {
    List<Genre> getAll();
    Genre getById(Long id);
    Genre create(String name);
    Boolean delete(Long id);
}