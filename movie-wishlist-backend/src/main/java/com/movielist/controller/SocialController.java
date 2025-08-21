package com.movielist.controller;

import com.movielist.entity.User;
import com.movielist.exception.ApiException;
import com.movielist.exception.ResourceNotFoundException;
import com.movielist.payload.ApiResponse;
import com.movielist.payload.CommentRequest;
import com.movielist.payload.CommentResponse;
import com.movielist.repository.UserRepository;
import com.movielist.service.SocialService;
import com.movielist.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
 
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/social")
public class SocialController {

    private static final Logger logger = LoggerFactory.getLogger(SocialController.class);

    @Autowired
    private SocialService socialService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @PostMapping("/movies/{movieId}/like")
    public ResponseEntity<ApiResponse> likeMovie(@PathVariable Long movieId) {
        try {
            User user = userService.getOrCreateDefaultUser();
            
            socialService.likeMovie(movieId, user);
            return ResponseEntity.ok(new ApiResponse(true, "Movie liked successfully"));
        } catch (ResourceNotFoundException e) {
            logger.error("Resource not found: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Error liking movie with ID {}: {}", movieId, e.getMessage());
            throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to like movie: " + e.getMessage());
        }
    }

    @DeleteMapping("/movies/{movieId}/unlike")
    public ResponseEntity<ApiResponse> unlikeMovie(@PathVariable Long movieId) {
        try {
            User user = userService.getOrCreateDefaultUser();
            
            socialService.unlikeMovie(movieId, user);
            return ResponseEntity.ok(new ApiResponse(true, "Movie unliked successfully"));
        } catch (ResourceNotFoundException e) {
            logger.error("Resource not found: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Error unliking movie with ID {}: {}", movieId, e.getMessage());
            throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to unlike movie: " + e.getMessage());
        }
    }

    @PostMapping("/movies/{movieId}/comments")
    public ResponseEntity<CommentResponse> addComment(
            @PathVariable Long movieId,
            @Valid @RequestBody CommentRequest commentRequest) {
        try {
            User user = userService.getOrCreateDefaultUser();
            
            CommentResponse comment = socialService.addComment(movieId, commentRequest, user);
            return new ResponseEntity<>(comment, HttpStatus.CREATED);
        } catch (ResourceNotFoundException e) {
            logger.error("Resource not found: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Error adding comment to movie with ID {}: {}", movieId, e.getMessage());
            throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to add comment: " + e.getMessage());
        }
    }

    @GetMapping("/movies/{movieId}/comments")
    public ResponseEntity<List<CommentResponse>> getMovieComments(@PathVariable Long movieId) {
        try {
            List<CommentResponse> comments = socialService.getMovieComments(movieId);
            return ResponseEntity.ok(comments);
        } catch (ResourceNotFoundException e) {
            logger.error("Movie not found: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Error getting comments for movie with ID {}: {}", movieId, e.getMessage());
            throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to get movie comments: " + e.getMessage());
        }
    }

    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<ApiResponse> deleteComment(@PathVariable Long commentId) {
        try {
            User user = userService.getOrCreateDefaultUser();
            
            socialService.deleteComment(commentId, user);
            return ResponseEntity.ok(new ApiResponse(true, "Comment deleted successfully"));
        } catch (ResourceNotFoundException e) {
            logger.error("Resource not found: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Error deleting comment with ID {}: {}", commentId, e.getMessage());
            throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to delete comment: " + e.getMessage());
        }
    }

    @GetMapping("/users/{userId}/likes")
    public ResponseEntity<List<Long>> getUserLikedMovies(@PathVariable Long userId) {
        try {
            List<Long> likedMovieIds = socialService.getUserLikedMovies(userId);
            return ResponseEntity.ok(likedMovieIds);
        } catch (ResourceNotFoundException e) {
            logger.error("User not found: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Error getting liked movies for user with ID {}: {}", userId, e.getMessage());
            throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to get user liked movies: " + e.getMessage());
        }
    }
}