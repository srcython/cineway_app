package com.srcython.cineway.domain.model

data class MovieDetail(
    val id: Int,
    val title: String,
    val releaseDate: String?,
    val runtime: Int,
    val genres: List<String>,
    val overview: String,
    val posterPath: String?,
    val backdropPath: String?,
    val voteAverage: Double
)
