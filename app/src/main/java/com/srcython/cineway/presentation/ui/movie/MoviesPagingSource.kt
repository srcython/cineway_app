package com.srcython.cineway.presentation.ui.movie

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.srcython.cineway.data.api.MovieApi
import com.srcython.cineway.data.mapper.MovieMapper
import com.srcython.cineway.domain.model.Movie

class MoviesPagingSource(
    private val movieApi: MovieApi,
    private val movieMapper: MovieMapper,
    private val searchQuery: String = "",
    private val genres: String? = null,
    private val sortBy: String,
) : PagingSource<Int, Movie>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        return try {
            val page = params.key ?: 1
            val response = if (searchQuery.isNotEmpty()) {
                movieApi.searchMovies(
                    query = searchQuery,
                    page = page
                )
            } else {
                movieApi.getFilteredMovies(
                    language = "en-US",
                    sortBy = sortBy,
                    genres = genres?.ifEmpty { null },
                    page = page
                )
            }

            val movies = response.results.map { movieMapper.mapToDomain(it) }

            LoadResult.Page(
                data = movies,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (page >= response.totalPages) null else page + 1
            )
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }


    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}
