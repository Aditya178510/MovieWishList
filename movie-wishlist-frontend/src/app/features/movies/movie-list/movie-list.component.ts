import { Component, Input, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Movie } from '../../../core/models/movie.model';
import { MovieService } from '../../../core/services/movie.service';

@Component({
  selector: 'app-movie-list',
  templateUrl: './movie-list.component.html',
  styleUrls: ['./movie-list.component.css']
})
export class MovieListComponent implements OnInit {
  @Input() movies: Movie[] = [];
  @Input() showActions: boolean = true;
  @Input() isWishlist: boolean = false;
  @Input() isWatched: boolean = false;
  @Input() isDiscover: boolean = false;

  constructor(
    private movieService: MovieService,
    private router: Router
  ) { }

  ngOnInit(): void {
  }

  ceil(value: number): number {
    return Math.ceil(value);
  }

  viewDetails(id: any): void {
    // If this is an OMDB movie (string ID starting with 'tt'), we need to add it first
    if (typeof id === 'string' && id.startsWith('tt')) {
      // Find the movie in our list
      const movie = this.movies.find(m => String(m.id) === id);
      if (movie) {
        this.addNewMovie(movie, new Event('click'));
        return;
      }
    }
    
    this.router.navigate(['/movies/detail', id]);
  }

  editMovie(id: number, event: Event): void {
    event.stopPropagation();
    this.router.navigate(['/movies/edit', id]);
  }

  deleteMovie(id: number, event: Event): void {
    event.stopPropagation();
    if (confirm('Are you sure you want to delete this movie?')) {
      this.movieService.deleteMovie(id).subscribe(() => {
        this.movies = this.movies.filter(movie => movie.id !== id);
      });
    }
  }

  markAsWatched(id: number, event: Event): void {
    event.stopPropagation();
    const movie = this.movies.find(m => m.id === id);
    if (movie) {
      this.movieService.markAsWatched(id, movie.rating, movie.review).subscribe((updated) => {
        if (this.isWishlist) {
          this.movies = this.movies.filter(movie => movie.id !== id);
        } else {
          const index = this.movies.findIndex(m => m.id === id);
          if (index !== -1) {
            this.movies[index] = { ...movie, status: 'WATCHED' } as Movie;
          }
        }
      });
    }
  }

  addToWishlist(id: number, event: Event): void {
    event.stopPropagation();
    const movie = this.movies.find(m => m.id === id);
    if (movie) {
      const updatedMovie: Movie = { ...movie, status: 'WISHLIST' };
      this.movieService.updateMovie(id, updatedMovie).subscribe(() => {
        if (this.isWatched) {
          this.movies = this.movies.filter(movie => movie.id !== id);
        } else {
          const index = this.movies.findIndex(m => m.id === id);
          if (index !== -1) {
            this.movies[index] = { ...updatedMovie };
          }
        }
      });
    }
  }

  addNewMovie(movie: any, event: Event): void {
    event.stopPropagation();
    const newMovie: Movie = {
      id: 0,
      title: movie.Title || movie.title,
      releaseYear: movie.Year ? parseInt(movie.Year) : new Date().getFullYear(),
      posterUrl: movie.Poster && movie.Poster !== 'N/A' ? movie.Poster : '',
      status: 'WISHLIST',
      genre: movie.Genre || '',
      runtime: movie.Runtime ? parseInt(movie.Runtime) : 0,
      rating: undefined,
      review: undefined,
      userId: 0,
      username: '',
      likesCount: 0,
      commentsCount: 0,
      userLiked: false
    };

    this.movieService.addMovie(newMovie).subscribe(addedMovie => {
      if (this.isWishlist) {
        this.movies.push(addedMovie);
      }
      alert('Movie added to wishlist!');
    });
  }

  likeMovie(id: number, event: Event): void {
    event.stopPropagation();
    const movie = this.movies.find(m => m.id === id);
    if (movie) {
      if (movie.userLiked) {
        this.movieService.unlikeMovie(id).subscribe(() => {
          const index = this.movies.findIndex(m => m.id === id);
          if (index !== -1) {
            this.movies[index] = {
              ...movie,
              userLiked: false,
              likesCount: (movie.likesCount || 0) - 1
            };
          }
        });
      } else {
        this.movieService.likeMovie(id).subscribe(() => {
          const index = this.movies.findIndex(m => m.id === id);
          if (index !== -1) {
            this.movies[index] = {
              ...movie,
              userLiked: true,
              likesCount: (movie.likesCount || 0) + 1
            };
          }
        });
      }
    }
  }
}