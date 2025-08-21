package com.movielist.controller;

import com.movielist.service.OmdbService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/omdb")
public class OmdbController {

    private final OmdbService omdbService;

    public OmdbController(OmdbService omdbService) {
        this.omdbService = omdbService;
    }

    // Search movies
    @GetMapping("/search")
    public ResponseEntity<String> search(@RequestParam String query) {
        return ResponseEntity.ok(omdbService.searchMovies(query));
    }

    // Get movie details
    @GetMapping("/details/{imdbId}")
    public ResponseEntity<String> getDetails(@PathVariable String imdbId) {
        return ResponseEntity.ok(omdbService.getMovieDetails(imdbId));
    }
}
