package com.srcython.cineway.domain.usecase

import com.srcython.cineway.domain.model.Review
import com.srcython.cineway.domain.repository.MovieRepository
import com.srcython.cineway.utils.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMovieReviewsUseCase @Inject constructor(
    private val repository: MovieRepository
) {
    operator fun invoke(movieId: Int): Flow<Resource<List<Review>>> {
        return repository.getMovieReviews(movieId)
    }
}