package com.controllers;

import com.entities.Genre;
import com.services.GenreService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/genres")
public class GenreController {

    private final GenreService genreService;

    public GenreController(GenreService genreService) {
        this.genreService = genreService;
    }

    @GetMapping
    public List<Genre> getAll() {
        return genreService.getAll();
    }

    @GetMapping("/{id}")
    public Genre getById(@PathVariable("id") Long id) {
        return genreService.getById(id);
    }

    // Optionnel (admin)
    @PostMapping
    public Genre create(@RequestBody GenreCreateRequest body) {
        return genreService.create(body.name());
    }

    // Optionnel
    @DeleteMapping("/{id}")
    public Boolean delete(@PathVariable("id") Long id) {
        return genreService.delete(id);
    }

    public record GenreCreateRequest(String name) {}
}