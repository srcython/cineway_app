package com.srcython.cineway.presentation.ui.movie.movielist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.srcython.cineway.domain.model.Genre
import com.srcython.cineway.domain.model.Movie
import com.srcython.cineway.domain.usecase.GetFilteredMoviesUseCase
import com.srcython.cineway.domain.usecase.GetMoviesUseCase
import com.srcython.cineway.domain.usecase.SearchMoviesUseCase
import com.srcython.cineway.domain.usecase.GetGenresUseCase
import com.srcython.cineway.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieListViewModel @Inject constructor(
    private val getMoviesUseCase: GetMoviesUseCase,
    private val searchMoviesUseCase: SearchMoviesUseCase,
    private val getFilteredMoviesUseCase: GetFilteredMoviesUseCase,
    private val getGenresUseCase: GetGenresUseCase
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    private val _sortBy = MutableStateFlow("popularity.desc")
    private val _selectedGenres = MutableStateFlow<List<Int>>(emptyList())
    private val _genres = MutableStateFlow<Resource<List<Genre>>>(Resource.Loading())

    val searchQuery: StateFlow<String> get() = _searchQuery
    val sortBy: StateFlow<String> get() = _sortBy
    val selectedGenres: StateFlow<List<Int>> get() = _selectedGenres
    val genres: StateFlow<Resource<List<Genre>>> get() = _genres

    @OptIn(ExperimentalCoroutinesApi::class)
    val movies: Flow<PagingData<Movie>> =
        combine(_searchQuery, _sortBy, _selectedGenres) { query, sortBy, selectedGenres ->
            Triple(query, sortBy, selectedGenres)
        }.flatMapLatest { (query, sortBy, selectedGenres) ->
            if (query.isEmpty()) {
                getFilteredMoviesUseCase(genres = selectedGenres.joinToString(","), sortBy = sortBy)
            } else {
                searchMoviesUseCase(query)
            }
        }.cachedIn(viewModelScope)

    init {
        fetchGenres()
    }

    private fun fetchGenres() {
        viewModelScope.launch {
            getGenresUseCase().collect { result ->
                _genres.value = result
            }
        }
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    fun getFilteredMovies(sortBy: String, genres: List<Int>) {
        _sortBy.value = sortBy
        _selectedGenres.value = genres
    }
}
