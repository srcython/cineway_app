package com.srcython.cineway.domain.usecase

import com.srcython.cineway.domain.model.Genre
import com.srcython.cineway.domain.repository.MovieRepository
import com.srcython.cineway.utils.Resource
import kotlinx.coroutines.flow.Flow

class GetGenresUseCase(private val movieRepository: MovieRepository) {
    suspend operator fun invoke(): Flow<Resource<List<Genre>>> {
        return movieRepository.getGenres()
    }
}
