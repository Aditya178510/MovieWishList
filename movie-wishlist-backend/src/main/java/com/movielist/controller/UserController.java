package com.movielist.controller;

import com.movielist.entity.User;
import com.movielist.exception.ApiException;
import com.movielist.exception.ResourceNotFoundException;
import com.movielist.payload.ApiResponse;
import com.movielist.payload.UserProfileRequest;
import com.movielist.payload.UserProfileResponse;
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
@RequestMapping("/api/users")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @GetMapping("/me")
    public ResponseEntity<UserProfileResponse> getCurrentUserProfile() {
        try {
            UserProfileResponse userProfile = userService.getUserProfile("guest");
            return ResponseEntity.ok(userProfile);
        } catch (ResourceNotFoundException e) {
            logger.error("User profile not found", e);
            throw e;
        } catch (Exception e) {
            logger.error("Error retrieving current user profile", e);
            throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "Error retrieving user profile");
        }
    }

    @GetMapping("/{username}")
    public ResponseEntity<UserProfileResponse> getUserProfile(@PathVariable String username) {
        try {
            UserProfileResponse userProfile = userService.getUserProfile(username);
            return ResponseEntity.ok(userProfile);
        } catch (ResourceNotFoundException e) {
            logger.error("User profile not found for username: {}", username, e);
            throw e;
        } catch (Exception e) {
            logger.error("Error retrieving user profile for username: {}", username, e);
            throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "Error retrieving user profile");
        }
    }

    @PutMapping("/me")
    public ResponseEntity<UserProfileResponse> updateUserProfile(@Valid @RequestBody UserProfileRequest profileRequest) {
        try {
            UserProfileResponse updatedProfile = userService.updateUserProfile("guest", profileRequest);
            logger.info("User profile updated successfully for: guest");
            return ResponseEntity.ok(updatedProfile);
        } catch (ResourceNotFoundException e) {
            logger.error("User not found during profile update", e);
            throw e;
        } catch (Exception e) {
            logger.error("Error updating user profile", e);
            throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "Error updating user profile");
        }
    }

    @PostMapping("/follow/{username}")
    public ResponseEntity<ApiResponse> followUser(@PathVariable String username) {
        try {
            userService.followUser("guest", username);
            logger.info("User guest is now following {}", username);
            return ResponseEntity.ok(new ApiResponse(true, "You are now following " + username));
        } catch (ResourceNotFoundException e) {
            logger.error("User not found during follow operation: {}", username, e);
            throw e;
        } catch (Exception e) {
            logger.error("Error following user: {}", username, e);
            throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "Error following user");
        }
    }

    @PostMapping("/unfollow/{username}")
    public ResponseEntity<ApiResponse> unfollowUser(@PathVariable String username) {
        try {
            userService.unfollowUser("guest", username);
            logger.info("User guest has unfollowed {}", username);
            return ResponseEntity.ok(new ApiResponse(true, "You have unfollowed " + username));
        } catch (ResourceNotFoundException e) {
            logger.error("User not found during unfollow operation: {}", username, e);
            throw e;
        } catch (Exception e) {
            logger.error("Error unfollowing user: {}", username, e);
            throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "Error unfollowing user");
        }
    }

    @GetMapping("/followers")
    public ResponseEntity<List<UserProfileResponse>> getFollowers() {
        try {
            List<UserProfileResponse> followers = userService.getFollowers("guest");
            return ResponseEntity.ok(followers);
        } catch (ResourceNotFoundException e) {
            logger.error("User not found when retrieving followers", e);
            throw e;
        } catch (Exception e) {
            logger.error("Error retrieving followers", e);
            throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "Error retrieving followers");
        }
    }

    @GetMapping("/following")
    public ResponseEntity<List<UserProfileResponse>> getFollowing() {
        try {
            List<UserProfileResponse> following = userService.getFollowing("guest");
            return ResponseEntity.ok(following);
        } catch (ResourceNotFoundException e) {
            logger.error("User not found when retrieving following users", e);
            throw e;
        } catch (Exception e) {
            logger.error("Error retrieving following users", e);
            throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "Error retrieving following users");
        }
    }

    @GetMapping("/leaderboard")
    public ResponseEntity<List<UserProfileResponse>> getLeaderboard() {
        try {
            List<UserProfileResponse> leaderboard = userService.getLeaderboard();
            return ResponseEntity.ok(leaderboard);
        } catch (Exception e) {
            logger.error("Error retrieving leaderboard", e);
            throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "Error retrieving leaderboard");
        }
    }
}