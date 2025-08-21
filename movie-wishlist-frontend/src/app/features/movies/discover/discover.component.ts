import { Component, OnInit } from '@angular/core';
import { MovieService } from '../../../core/services/movie.service';

@Component({
  selector: 'app-discover',
  templateUrl: './discover.component.html',
  styleUrls: ['./discover.component.css']
})
export class DiscoverComponent implements OnInit {
  searchQuery = '';
  searchResults: any[] = [];
  trendingMovies: any[] = [];
  loading = false;
  searchLoading = false;
  error = '';

  constructor(private movieService: MovieService) {}

  ngOnInit(): void {
    this.loadTrendingMovies();
  }

  loadTrendingMovies(): void {
    this.loading = true;
    // OMDb has no "trending", so we fake it with a default search (e.g. Avengers)
    this.movieService.searchMovies('Avengers').subscribe({
      next: (data: any) => {
        // Ensure we have the Search array and process each movie to ensure it has proper structure
        if (data && data.Search && Array.isArray(data.Search)) {
          this.trendingMovies = data.Search.map((movie: any) => ({
            id: movie.imdbID,
            title: movie.Title,
            releaseYear: movie.Year ? parseInt(movie.Year) : null,
            posterUrl: movie.Poster && movie.Poster !== 'N/A' ? movie.Poster : '',
            genre: '',
            status: null
          }));
        } else {
          this.trendingMovies = [];
        }
        this.loading = false;
      },
      error: (error) => {
        this.error = 'Failed to load trending movies. Please try again later.';
        console.error('Error loading trending movies:', error);
        this.loading = false;
      }
    });
  }

  searchMovies(): void {
    if (!this.searchQuery.trim()) {
      return;
    }

    this.searchLoading = true;
    this.movieService.searchMovies(this.searchQuery).subscribe({
      next: (data: any) => {
        // Ensure we have the Search array and process each movie to ensure it has proper structure
        if (data && data.Search && Array.isArray(data.Search)) {
          this.searchResults = data.Search.map((movie: any) => ({
            id: movie.imdbID,
            title: movie.Title,
            releaseYear: movie.Year ? parseInt(movie.Year) : null,
            posterUrl: movie.Poster && movie.Poster !== 'N/A' ? movie.Poster : '',
            genre: '',
            status: null
          }));
        } else {
          this.searchResults = [];
        }
        this.searchLoading = false;
      },
      error: (error) => {
        this.error = 'Failed to search movies. Please try again later.';
        console.error('Error searching movies:', error);
        this.searchLoading = false;
      }
    });
    }
  
    clearSearch(): void {
    this.searchQuery = '';
    this.searchResults = [];
  }
}
