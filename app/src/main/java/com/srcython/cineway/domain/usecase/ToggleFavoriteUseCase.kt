package com.srcython.cineway.domain.usecase

import com.srcython.cineway.domain.repository.DbRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ToggleFavoriteUseCase @Inject constructor(
    private val dbRepository: DbRepository
) {

    fun isMovieFavorite(movieId: Int): Flow<Boolean> {
        return dbRepository.isMovieFavorite(movieId)
    }

    suspend fun addMovieToFavorites(movieId: Int) {
        dbRepository.addMovieToFavorites(movieId)
    }

    suspend fun removeMovieFromFavorites(movieId: Int) {
        dbRepository.removeMovieFromFavorites(movieId)
    }

    suspend fun getAllFavoriteMovieIds(): List<Int> {
        return dbRepository.getAllFavoriteMovieIds()
    }
}
