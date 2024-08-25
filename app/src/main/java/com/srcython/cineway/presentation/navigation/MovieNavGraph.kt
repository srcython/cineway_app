package com.srcython.cineway.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.srcython.cineway.presentation.ui.movie.movielist.MovieListScreen
import com.srcython.cineway.presentation.ui.movie.moviedetail.TrailerScreen
import com.srcython.cineway.presentation.ui.movie.favoritemovie.FavoriteMovieScreen

@Composable
fun MovieNavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "movie_list") {
        composable("movie_list") {
            MovieListScreen(navController = navController)
        }
        composable("detail/{movieId}") { backStackEntry ->
            val movieId = backStackEntry.arguments?.getString("movieId")?.toInt() ?: 0
            TrailerScreen(movieId = movieId, navController = navController)
        }
        composable("favorite_movies") {
            FavoriteMovieScreen(navController = navController)
        }
    }
}
