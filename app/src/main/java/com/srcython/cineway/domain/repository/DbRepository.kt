package com.srcython.cineway.domain.repository

import kotlinx.coroutines.flow.Flow

interface DbRepository {
    suspend fun addMovieToFavorites(movieId: Int)
    suspend fun removeMovieFromFavorites(movieId: Int)
    fun isMovieFavorite(movieId: Int): Flow<Boolean>
    suspend fun getAllFavoriteMovieIds(): List<Int>
}
