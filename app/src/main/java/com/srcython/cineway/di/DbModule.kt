package com.srcython.cineway.di

import android.content.Context
import androidx.room.Room
import com.srcython.cineway.data.local.dao.MovieDao
import com.srcython.cineway.data.local.database.AppDatabase
import com.srcython.cineway.data.repository.DbRepositoryImpl
import com.srcython.cineway.domain.repository.DbRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DbModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "cineway_db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideMovieDao(appDatabase: AppDatabase): MovieDao {
        return appDatabase.movieDao()
    }

    @Provides
    @Singleton
    fun provideDbRepository(
        movieDao: MovieDao
    ): DbRepository {
        return DbRepositoryImpl(movieDao)
    }
}
