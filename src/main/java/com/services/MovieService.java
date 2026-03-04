package com.services;

import com.dtos.MovieDto;

import java.util.List;

public interface MovieService {
    List<MovieDto> getAllMovies();
    MovieDto getMovieById(Long id);
    MovieDto createMovie(MovieDto dto);
    MovieDto updateMovie(Long id, MovieDto dto);
    MovieDto openMovie(Long id, Double prix);
    MovieDto closeMovie(Long id);
}