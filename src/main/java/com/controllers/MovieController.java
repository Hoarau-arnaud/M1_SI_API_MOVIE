package com.controllers;

import com.dtos.MovieDto;
import com.services.MovieService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/movies")
public class MovieController {

    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping
    public List<MovieDto> getAll() {
        return movieService.getAllMovies();
    }

    @GetMapping("/{id}")
    public MovieDto getById(@PathVariable("id") Long id) {
        return movieService.getMovieById(id);
    }

    @PostMapping
    public MovieDto create(@RequestBody MovieDto dto) {
        return movieService.createMovie(dto);
    }

    @PutMapping("/{id}")
    public MovieDto update(@PathVariable("id") Long id, @RequestBody MovieDto dto) {
        return movieService.updateMovie(id, dto);
    }

    @PutMapping("/{id}/open")
    public MovieDto open(@PathVariable("id") Long id, @RequestParam("prix") Double prix) {
        return movieService.openMovie(id, prix);
    }

    @PutMapping("/{id}/close")
    public MovieDto close(@PathVariable("id") Long id) {
        return movieService.closeMovie(id);
    }
}