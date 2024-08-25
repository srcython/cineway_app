package com.srcython.cineway.data.mapper

import com.srcython.cineway.data.model.MovieDetailResponseDto
import com.srcython.cineway.domain.model.MovieDetail

class MovieDetailMapper {

    fun mapToDomain(movieDetailResponse: MovieDetailResponseDto): MovieDetail {
        return MovieDetail(
            id = movieDetailResponse.id,
            title = movieDetailResponse.title,
            releaseDate = movieDetailResponse.releaseDate,
            runtime = movieDetailResponse.runtime,
            genres = movieDetailResponse.genres.map { it.name },
            overview = movieDetailResponse.overview,
            posterPath = movieDetailResponse.posterPath ?: "",
            backdropPath = movieDetailResponse.backdropPath ?: "",
            voteAverage = formatVoteAverage(movieDetailResponse.voteAverage)
        )
    }
    private fun formatVoteAverage(voteAverage: Double): Double {
        return String.format("%.1f", voteAverage).toDouble()
    }
}
