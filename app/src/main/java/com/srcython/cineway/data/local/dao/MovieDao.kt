package com.srcython.cineway.data.local.dao

import androidx.room.*
import com.srcython.cineway.data.local.entities.FavoriteMovieEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavoriteMovie(favoriteMovie: FavoriteMovieEntity)

    @Delete
    suspend fun deleteFavoriteMovie(favoriteMovie: FavoriteMovieEntity)

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_movies WHERE movieId = :movieId)")
    fun isFavorite(movieId: Int): Flow<Boolean>

    @Query("SELECT movieId FROM favorite_movies")
    suspend fun getAllFavoriteMovieIds(): List<Int>
}
