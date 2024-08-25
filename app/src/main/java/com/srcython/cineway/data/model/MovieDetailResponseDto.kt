package com.srcython.cineway.data.model

import com.google.gson.annotations.SerializedName

data class MovieDetailResponseDto(
    @SerializedName("id")
    val id: Int,

    @SerializedName("title")
    val title: String,

    @SerializedName("release_date")
    val releaseDate: String,

    @SerializedName("runtime")
    val runtime: Int,

    @SerializedName("genres")
    val genres: List<GenreDto>,

    @SerializedName("overview")
    val overview: String,

    @SerializedName("poster_path")
    val posterPath: String?,

    @SerializedName("backdrop_path")
    val backdropPath: String?,

    @SerializedName("vote_average")
    val voteAverage: Double
)
