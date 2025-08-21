import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

const routes: Routes = [
  {
    path: '',
    redirectTo: '/movies/wishlist',
    pathMatch: 'full'
  },
  {
    path: 'auth',
    loadChildren: () => import('./features/auth/auth.module').then(m => m.AuthModule)
  },
  {
    path: 'movies',
    loadChildren: () => import('./features/movies/movies.module').then(m => m.MoviesModule),
    // authentication removed; route is public
  },
  {
    path: 'discover',
    redirectTo: '/movies/discover',
    pathMatch: 'full'
  },
  {
    path: '**',
    redirectTo: '/movies/wishlist'
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }