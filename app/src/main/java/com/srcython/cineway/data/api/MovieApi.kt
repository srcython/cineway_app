package com.srcython.cineway.data.api

import com.srcython.cineway.data.model.CreditsResponseDto
import com.srcython.cineway.data.model.GenreResponseDto
import com.srcython.cineway.data.model.MovieDetailResponseDto
import com.srcython.cineway.data.model.MoviesResponseDto
import com.srcython.cineway.data.model.ReviewResponseDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieApi {

    @GET("discover/movie")
    suspend fun getFilteredMovies(
        @Query("language") language: String = "en-US",
        @Query("sort_by") sortBy: String,
        @Query("with_genres") genres: String? = null,
        @Query("page") page: Int
    ): MoviesResponseDto

    @GET("search/movie")
    suspend fun searchMovies(
        @Query("query") query: String,
        @Query("page") page: Int
    ): MoviesResponseDto

    @GET("genre/movie/list")
    suspend fun getGenres(
        @Query("language") language: String = "en-US"
    ): GenreResponseDto

    @GET("movie/{movieId}")
    suspend fun fetchMovieDetails(
        @Path("movieId") movieId: Int
    ): MovieDetailResponseDto

    @GET("movie/{movieId}/reviews")
    suspend fun getMovieReviews(
        @Path("movieId") movieId: Int
    ): ReviewResponseDto

    @GET("movie/{movieId}/credits")
    suspend fun getMovieCredits(
        @Path("movieId") movieId: Int
    ): CreditsResponseDto
}