package com.srcython.cineway.data.mapper

import com.srcython.cineway.data.model.MovieDto
import com.srcython.cineway.domain.model.Movie

class MovieMapper {
    fun mapToDomain(dto: MovieDto): Movie {
        return Movie(
            id = dto.id,
            title = dto.title,
            overview = dto.overview,
            posterPath = dto.posterPath,
            releaseDate = dto.releaseDate,
            voteAverage = formatVoteAverage(dto.voteAverage)
        )
    }

    private fun formatVoteAverage(voteAverage: Double): Double {
        return String.format("%.1f", voteAverage).toDouble()
    }
}