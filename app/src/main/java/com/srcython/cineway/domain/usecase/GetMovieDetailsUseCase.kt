package com.srcython.cineway.domain.usecase

import com.srcython.cineway.domain.model.MovieDetail
import com.srcython.cineway.domain.repository.MovieRepository
import com.srcython.cineway.utils.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMovieDetailsUseCase @Inject constructor(
    private val movieRepository: MovieRepository
) {
    operator fun invoke(movieId: Int): Flow<Resource<MovieDetail>> {
        return movieRepository.getMovieDetails(movieId)
    }
}
