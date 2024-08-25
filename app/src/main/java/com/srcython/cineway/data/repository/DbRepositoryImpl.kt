package com.srcython.cineway.data.repository

import com.srcython.cineway.data.local.dao.MovieDao
import com.srcython.cineway.data.local.entities.FavoriteMovieEntity
import com.srcython.cineway.domain.repository.DbRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DbRepositoryImpl @Inject constructor(
    private val movieDao: MovieDao
) : DbRepository {

    override suspend fun addMovieToFavorites(movieId: Int) {
        movieDao.insertFavoriteMovie(FavoriteMovieEntity(movieId))
    }

    override suspend fun removeMovieFromFavorites(movieId: Int) {
        movieDao.deleteFavoriteMovie(FavoriteMovieEntity(movieId))
    }

    override fun isMovieFavorite(movieId: Int): Flow<Boolean> {
        return movieDao.isFavorite(movieId)
    }

    override suspend fun getAllFavoriteMovieIds(): List<Int> {
        return movieDao.getAllFavoriteMovieIds()
    }
}
