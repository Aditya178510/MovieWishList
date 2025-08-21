import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { environment } from '../../../environments/environment';
import { Movie } from '../models/movie.model';

@Injectable({
  providedIn: 'root'
})
export class MovieService {
  private apiUrl = `${environment.apiUrl}/api/movies`;

  constructor(private http: HttpClient) { }

  // Error handling method
  private handleError(error: HttpErrorResponse) {
    let errorMessage = 'An unknown error occurred';
    if (error.error instanceof ErrorEvent) {
      // Client-side error
      errorMessage = `Error: ${error.error.message}`;
    } else {
      // Server-side error
      errorMessage = `Error Code: ${error.status}\nMessage: ${error.message}`;
    }
    console.error(errorMessage);
    return throwError(() => new Error(errorMessage));
  }

  // Get a single movie by ID
  getMovie(id: number): Observable<Movie> {
    return this.http.get<Movie>(`${this.apiUrl}/${id}`).pipe(
      catchError(this.handleError)
    );
  }

  // Add a new movie
  addMovie(movie: Movie): Observable<Movie> {
    const requestBody = {
      title: movie.title,
      genre: movie.genre,
      releaseYear: movie.releaseYear,
      runtime: movie.runtime,
      posterUrl: movie.posterUrl,
      rating: movie.rating,
      review: movie.review
    };
    return this.http.post<Movie>(this.apiUrl, requestBody).pipe(
      catchError(this.handleError)
    );
  }

  // Update an existing movie (metadata)
  updateMovie(id: number, movie: Movie): Observable<Movie> {
    const requestBody = {
      title: movie.title,
      genre: movie.genre,
      releaseYear: movie.releaseYear,
      runtime: movie.runtime,
      posterUrl: movie.posterUrl,
      rating: movie.rating,
      review: movie.review
    };
    return this.http.put<Movie>(`${this.apiUrl}/${id}`, requestBody).pipe(
      catchError(this.handleError)
    );
  }

  // Mark a movie as watched
  markAsWatched(id: number, rating?: number, review?: string): Observable<Movie> {
    const params: string[] = [];
    if (typeof rating === 'number') params.push(`rating=${rating}`);
    if (typeof review === 'string' && review.length > 0) params.push(`review=${encodeURIComponent(review)}`);
    const qs = params.length ? `?${params.join('&')}` : '';
    return this.http.put<Movie>(`${this.apiUrl}/${id}/mark-watched${qs}`, {}).pipe(
      catchError(this.handleError)
    );
  }

  // Delete a movie
  deleteMovie(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`).pipe(
      catchError(this.handleError)
    );
  }

  // Get movies for wishlist
  getWishlist(): Observable<Movie[]> {
    return this.http.get<Movie[]>(`${this.apiUrl}/wishlist`).pipe(
      catchError(this.handleError)
    );
  }

  // Get watched movies
  getWatched(): Observable<Movie[]> {
    return this.http.get<Movie[]>(`${this.apiUrl}/watched`).pipe(
      catchError(this.handleError)
    );
  }

  // Search for movies from external API
  searchMovies(query: string): Observable<any> {
    return this.http.get<any>(`${environment.apiUrl}/api/omdb/search?query=${encodeURIComponent(query)}`).pipe(
      catchError(this.handleError)
    );
  }

  // Get movie details by IMDB ID
  getMovieDetailsByImdbId(imdbId: string): Observable<any> {
    return this.http.get<any>(`${environment.apiUrl}/api/omdb/details/${imdbId}`).pipe(
      catchError(this.handleError)
    );
  }

  // Like a movie
  likeMovie(id: number): Observable<void> {
    return this.http.post<void>(`${environment.apiUrl}/api/social/movies/${id}/like`, {}).pipe(
      catchError(this.handleError)
    );
  }

  // Unlike a movie
  unlikeMovie(id: number): Observable<void> {
    return this.http.delete<void>(`${environment.apiUrl}/api/social/movies/${id}/unlike`).pipe(
      catchError(this.handleError)
    );
  }

  // Add a comment to a movie
  addComment(id: number, content: string): Observable<any> {
    return this.http.post<any>(`${environment.apiUrl}/api/social/movies/${id}/comments`, { content }).pipe(
      catchError(this.handleError)
    );
  }

  // Get comments for a movie
  getComments(id: number): Observable<any[]> {
    return this.http.get<any[]>(`${environment.apiUrl}/api/social/movies/${id}/comments`).pipe(
      catchError(this.handleError)
    );
  }
}