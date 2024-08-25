package com.srcython.cineway.data.mapper

import com.srcython.cineway.data.model.GenreDto
import com.srcython.cineway.domain.model.Genre

class GenreMapper {
    fun mapToDomain(dto: GenreDto): Genre {
        return Genre(
            id = dto.id,
            name = dto.name
        )
    }
}
