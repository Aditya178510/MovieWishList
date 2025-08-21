package com.movielist.controller;

import com.movielist.entity.Movie;
import com.movielist.entity.User;
import com.movielist.exception.ApiException;
import com.movielist.exception.ResourceNotFoundException;
import com.movielist.payload.ApiResponse;
import com.movielist.payload.MovieRequest;
import com.movielist.payload.MovieResponse;
import com.movielist.repository.MovieRepository;
import com.movielist.repository.UserRepository;
import com.movielist.service.UserService;
import com.movielist.service.MovieService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
 
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/movies")
public class MovieController {

    private static final Logger logger = LoggerFactory.getLogger(MovieController.class);

    @Autowired
    private MovieService movieService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @GetMapping("/wishlist")
    public ResponseEntity<List<MovieResponse>> getWishlistMovies() {
        try {
            User user = userService.getOrCreateDefaultUser();
            List<MovieResponse> wishlistMovies = movieService.getWishlistMovies(user);
            return ResponseEntity.ok(wishlistMovies);
        } catch (ResourceNotFoundException e) {
            logger.error("User not found: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Error getting wishlist movies: {}", e.getMessage());
            throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to get wishlist movies: " + e.getMessage());
        }
    }

    @GetMapping("/watched")
    public ResponseEntity<List<MovieResponse>> getWatchedMovies() {
        try {
            User user = userService.getOrCreateDefaultUser();
            List<MovieResponse> watchedMovies = movieService.getWatchedMovies(user);
            return ResponseEntity.ok(watchedMovies);
        } catch (ResourceNotFoundException e) {
            logger.error("User not found: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Error getting watched movies: {}", e.getMessage());
            throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to get watched movies: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<MovieResponse> getMovieById(@PathVariable Long id) {
        try {
            MovieResponse movie = movieService.getMovieById(id);
            return ResponseEntity.ok(movie);
        } catch (ResourceNotFoundException e) {
            logger.error("Movie not found: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Error getting movie by ID {}: {}", id, e.getMessage());
            throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to get movie details: " + e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<MovieResponse> addMovie(@Valid @RequestBody MovieRequest movieRequest) {
        try {
            User user = userService.getOrCreateDefaultUser();
            MovieResponse createdMovie = movieService.addMovie(movieRequest, user);
            return new ResponseEntity<>(createdMovie, HttpStatus.CREATED);
        } catch (ResourceNotFoundException e) {
            logger.error("User not found: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Error adding movie: {}", e.getMessage());
            throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to add movie: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<MovieResponse> updateMovie(@PathVariable Long id, @Valid @RequestBody MovieRequest movieRequest) {
        try {
            User user = userService.getOrCreateDefaultUser();
            MovieResponse updatedMovie = movieService.updateMovie(id, movieRequest, user);
            return ResponseEntity.ok(updatedMovie);
        } catch (ResourceNotFoundException e) {
            logger.error("Resource not found: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Error updating movie with ID {}: {}", id, e.getMessage());
            throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to update movie: " + e.getMessage());
        }
    }

    @PutMapping("/{id}/mark-watched")
    public ResponseEntity<MovieResponse> markAsWatched(@PathVariable Long id, @RequestParam(required = false) Integer rating, @RequestParam(required = false) String review) {
        try {
            User user = userService.getOrCreateDefaultUser();
            MovieResponse updatedMovie = movieService.markAsWatched(id, rating, review, user);
            return ResponseEntity.ok(updatedMovie);
        } catch (ResourceNotFoundException e) {
            logger.error("Resource not found: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Error marking movie with ID {} as watched: {}", id, e.getMessage());
            throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to mark movie as watched: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteMovie(@PathVariable Long id) {
        try {
            User user = userService.getOrCreateDefaultUser();
            movieService.deleteMovie(id, user);
            return ResponseEntity.ok(new ApiResponse(true, "Movie deleted successfully"));
        } catch (ResourceNotFoundException e) {
            logger.error("Resource not found: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Error deleting movie with ID {}: {}", id, e.getMessage());
            throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to delete movie: " + e.getMessage());
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<MovieResponse>> getUserMovies(@PathVariable Long userId, @RequestParam(required = false) Movie.Status status) {
        try {
            List<MovieResponse> movies = movieService.getUserMovies(userId, status);
            return ResponseEntity.ok(movies);
        } catch (ResourceNotFoundException e) {
            logger.error("User not found: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Error getting movies for user ID {}: {}", userId, e.getMessage());
            throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to get user movies: " + e.getMessage());
        }
    }
}