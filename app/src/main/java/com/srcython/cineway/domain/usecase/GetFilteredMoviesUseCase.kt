package com.srcython.cineway.domain.usecase

import androidx.paging.PagingData
import com.srcython.cineway.domain.model.Movie
import com.srcython.cineway.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFilteredMoviesUseCase @Inject constructor(
    private val repository: MovieRepository
) {
    suspend operator fun invoke(genres: String, sortBy: String): Flow<PagingData<Movie>> {
        return repository.getFilteredMovies(genres = genres, sortBy = sortBy)

    }
}