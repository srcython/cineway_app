package com.srcython.cineway.domain.usecase

import com.srcython.cineway.domain.model.CastMember
import com.srcython.cineway.domain.repository.MovieRepository
import com.srcython.cineway.utils.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMovieCreditsUseCase @Inject constructor(
    private val repository: MovieRepository
) {
    operator fun invoke(movieId: Int): Flow<Resource<List<CastMember>>> {
        return repository.getMovieCredits(movieId)
    }
}
