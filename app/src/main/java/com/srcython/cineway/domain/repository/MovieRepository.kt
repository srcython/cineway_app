package com.srcython.cineway.domain.repository

import androidx.paging.PagingData
import com.srcython.cineway.domain.model.CastMember
import com.srcython.cineway.domain.model.Genre
import com.srcython.cineway.domain.model.Movie
import com.srcython.cineway.domain.model.MovieDetail
import com.srcython.cineway.domain.model.Review
import com.srcython.cineway.utils.Resource
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    fun getMovies(): Flow<PagingData<Movie>>
    fun searchMovies(query: String): Flow<PagingData<Movie>>
    suspend fun getGenres(): Flow<Resource<List<Genre>>>
    fun getFilteredMovies(genres: String, sortBy: String): Flow<PagingData<Movie>>
    fun getMovieDetails(movieId: Int): Flow<Resource<MovieDetail>>
    fun getMovieReviews(movieId: Int): Flow<Resource<List<Review>>>
    fun getMovieCredits(movieId: Int): Flow<Resource<List<CastMember>>>
}
