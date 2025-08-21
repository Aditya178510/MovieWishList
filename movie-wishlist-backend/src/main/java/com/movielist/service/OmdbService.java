package com.movielist.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class OmdbService {

    @Value("${omdb.api.key}")
    private String apiKey;

    @Value("${omdb.api.url}")
    private String apiUrl;

    private final RestTemplate restTemplate;

    public OmdbService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    // Search movies
    public String searchMovies(String query) {
        String url = apiUrl + "?apikey=" + apiKey + "&s=" + query;
        return restTemplate.getForObject(url, String.class);
    }

    // Get movie details
    public String getMovieDetails(String imdbId) {
        String url = apiUrl + "?apikey=" + apiKey + "&i=" + imdbId + "&plot=full";
        return restTemplate.getForObject(url, String.class);
    }
}
