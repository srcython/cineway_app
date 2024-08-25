package com.srcython.cineway.di

import com.srcython.cineway.data.mapper.*
import com.srcython.cineway.data.repository.MovieRepositoryImpl
import com.srcython.cineway.domain.repository.MovieRepository
import com.srcython.cineway.domain.usecase.*
import com.srcython.cineway.data.api.MovieApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideMovieMapper(): MovieMapper {
        return MovieMapper()
    }

    @Provides
    @Singleton
    fun provideGenreMapper(): GenreMapper {
        return GenreMapper()
    }

    @Provides
    @Singleton
    fun provideMovieDetailMapper(): MovieDetailMapper {
        return MovieDetailMapper()
    }

    @Provides
    @Singleton
    fun provideReviewMapper(): ReviewMapper {
        return ReviewMapper()
    }

    @Provides
    @Singleton
    fun provideCastMapper(): CastMapper {
        return CastMapper()
    }

    @Provides
    @Singleton
    fun provideMovieRepository(
        api: MovieApi,
        movieMapper: MovieMapper,
        genreMapper: GenreMapper,
        movieDetailMapper: MovieDetailMapper,
        reviewMapper: ReviewMapper,
        castMapper: CastMapper
    ): MovieRepository {
        return MovieRepositoryImpl(
            api,
            movieMapper,
            movieDetailMapper,
            genreMapper,
            reviewMapper,
            castMapper
        )
    }

    @Provides
    @Singleton
    fun provideGetMoviesUseCase(movieRepository: MovieRepository): GetMoviesUseCase {
        return GetMoviesUseCase(movieRepository)
    }

    @Provides
    @Singleton
    fun provideSearchMoviesUseCase(movieRepository: MovieRepository): SearchMoviesUseCase {
        return SearchMoviesUseCase(movieRepository)
    }

    @Provides
    @Singleton
    fun provideGetGenresUseCase(movieRepository: MovieRepository): GetGenresUseCase {
        return GetGenresUseCase(movieRepository)
    }

    @Provides
    @Singleton
    fun provideGetFilteredMoviesUseCase(movieRepository: MovieRepository): GetFilteredMoviesUseCase {
        return GetFilteredMoviesUseCase(movieRepository)
    }
}
