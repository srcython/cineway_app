package com.srcython.cineway.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.srcython.cineway.data.local.dao.MovieDao
import com.srcython.cineway.data.local.entities.FavoriteMovieEntity

@Database(entities = [FavoriteMovieEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao
}
