package com.srcython.cineway.data.mapper

import com.srcython.cineway.data.model.CastDto
import com.srcython.cineway.domain.model.CastMember
import javax.inject.Inject

class CastMapper @Inject constructor() {
    fun mapToDomain(dto: CastDto): CastMember {
        return CastMember(
            id = dto.id,
            name = dto.name,
            character = dto.character,
            profilePath = dto.profile_path
        )
    }
}
