package com.movielist.config;

import com.movielist.entity.Movie;
import com.movielist.entity.User;
import com.movielist.repository.MovieRepository;
import com.movielist.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private UserService userService;

    @Override
    public void run(String... args) throws Exception {
        if (movieRepository.count() > 0) {
            return;
        }

        User guest = userService.getOrCreateDefaultUser();

        Movie m1 = new Movie();
        m1.setTitle("Inception");
        m1.setGenre("Sci-Fi");
        m1.setReleaseYear(2010);
        m1.setRuntime(148);
        m1.setPosterUrl("https://image.tmdb.org/t/p/w500/qmDpIHrmpJINaRKAfWQfftjCdyi.jpg");
        m1.setStatus(Movie.Status.WISHLIST);
        m1.setUser(guest);

        Movie m2 = new Movie();
        m2.setTitle("The Dark Knight");
        m2.setGenre("Action");
        m2.setReleaseYear(2008);
        m2.setRuntime(152);
        m2.setPosterUrl("https://image.tmdb.org/t/p/w500/qJ2tW6WMUDux911r6m7haRef0WH.jpg");
        m2.setStatus(Movie.Status.WATCHED);
        m2.setRating(5);
        m2.setReview("Masterpiece");
        m2.setUser(guest);

        Movie m3 = new Movie();
        m3.setTitle("Interstellar");
        m3.setGenre("Adventure");
        m3.setReleaseYear(2014);
        m3.setRuntime(169);
        m3.setPosterUrl("https://image.tmdb.org/t/p/w500/rAiYTfKGqDCRIIqo664sY9XZIvQ.jpg");
        m3.setStatus(Movie.Status.WATCHED);
        m3.setRating(5);
        m3.setReview("Emotional sci-fi epic");
        m3.setUser(guest);

        movieRepository.saveAll(Arrays.asList(m1, m2, m3));
    }
}


