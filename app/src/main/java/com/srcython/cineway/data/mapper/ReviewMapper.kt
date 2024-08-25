package com.srcython.cineway.data.mapper

import com.srcython.cineway.data.model.ReviewDto
import com.srcython.cineway.domain.model.Review
import javax.inject.Inject

class ReviewMapper @Inject constructor() {
    fun mapToDomain(dto: ReviewDto): Review {
        return Review(
            author = dto.author ?: "Unknown",
            content = dto.content ?: "No content available",
            rating = dto.rating ?: 0.0
        )
    }
}
