package com.srcython.cineway.data.repository

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.srcython.cineway.data.api.MovieApi
import com.srcython.cineway.data.mapper.CastMapper
import com.srcython.cineway.data.mapper.GenreMapper
import com.srcython.cineway.data.mapper.MovieDetailMapper
import com.srcython.cineway.data.mapper.MovieMapper
import com.srcython.cineway.data.mapper.ReviewMapper
import com.srcython.cineway.domain.model.CastMember
import com.srcython.cineway.domain.model.Genre
import com.srcython.cineway.domain.model.Movie
import com.srcython.cineway.domain.model.MovieDetail
import com.srcython.cineway.domain.model.Review
import com.srcython.cineway.domain.repository.MovieRepository
import com.srcython.cineway.presentation.ui.movie.MoviesPagingSource
import com.srcython.cineway.utils.Resource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
    private val api: MovieApi,
    private val movieMapper: MovieMapper,
    private val movieDetailMapper: MovieDetailMapper,
    private val genreMapper: GenreMapper,
    private val reviewMapper: ReviewMapper,
    private val castMapper: CastMapper
) : MovieRepository {

    override fun getMovies(): Flow<PagingData<Movie>> {
        return Pager(PagingConfig(pageSize = 20)) {
            MoviesPagingSource(api, movieMapper, sortBy = "popularity.desc")
        }.flow.cachedIn(CoroutineScope(Dispatchers.IO))
    }

    override fun searchMovies(
        query: String,
    ): Flow<PagingData<Movie>> {
        return Pager(PagingConfig(pageSize = 20)) {
            MoviesPagingSource(
                api,
                movieMapper,
                searchQuery = query,
                sortBy = ""
            )
        }.flow.cachedIn(CoroutineScope(Dispatchers.IO))
    }

    override fun getFilteredMovies(genres: String, sortBy: String): Flow<PagingData<Movie>> {
        return Pager(PagingConfig(pageSize = 20)) {
            MoviesPagingSource(api, movieMapper, genres, sortBy = sortBy)
        }.flow.cachedIn(CoroutineScope(Dispatchers.IO))
    }

    override suspend fun getGenres(): Flow<Resource<List<Genre>>> = flow {
        emit(Resource.Loading())
        try {
            val response = api.getGenres()
            val genres = response.genres.map { genreMapper.mapToDomain(it) }
            emit(Resource.Success(genres))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "An unknown error occurred"))
        }
    }.flowOn(Dispatchers.IO)

    override fun getMovieDetails(movieId: Int): Flow<Resource<MovieDetail>> = flow {
        emit(Resource.Loading())
        try {
            val response = api.fetchMovieDetails(movieId)
            val movieDetail = movieDetailMapper.mapToDomain(response)
            emit(Resource.Success(movieDetail))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "An unknown error occurred"))
        }
    }.flowOn(Dispatchers.IO)


    override fun getMovieReviews(movieId: Int): Flow<Resource<List<Review>>> = flow {
        emit(Resource.Loading())
        try {
            val response = api.getMovieReviews(movieId)
            val reviews = response.results.map { reviewMapper.mapToDomain(it) } ?: emptyList()
            emit(Resource.Success(reviews))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "An unknown error occurred"))
        }
    }.flowOn(Dispatchers.IO)


    override fun getMovieCredits(movieId: Int): Flow<Resource<List<CastMember>>> = flow {
        emit(Resource.Loading())
        try {
            val response = api.getMovieCredits(movieId)
            val castMembers = response.cast.map { castMapper.mapToDomain(it) }
            emit(Resource.Success(castMembers))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "An unknown error occurred"))
        }
    }.flowOn(Dispatchers.IO)

}

