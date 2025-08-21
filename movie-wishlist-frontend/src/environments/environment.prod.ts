export const environment = {
  production: true,
  apiUrl: '/api', // This will be proxied to the backend container in Docker
  omdbApiKey: '' // OMDB API key is handled by the backend
};