package com.srcython.cineway.presentation.ui.movie.favoritemovie

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.srcython.cineway.domain.model.MovieDetail
import com.srcython.cineway.domain.usecase.GetMovieDetailsUseCase
import com.srcython.cineway.domain.usecase.ToggleFavoriteUseCase
import com.srcython.cineway.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteMovieViewModel @Inject constructor(
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
    private val getMovieDetailsUseCase: GetMovieDetailsUseCase
) : ViewModel() {

    private val _isFavorite = MutableStateFlow(false)
    val isFavorite: StateFlow<Boolean> = _isFavorite

    private val _favoriteMovies = MutableStateFlow<List<MovieDetail>>(emptyList())
    val favoriteMovies: StateFlow<List<MovieDetail>> = _favoriteMovies

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun checkIfMovieIsFavorite(movieId: Int) {
        viewModelScope.launch {
            toggleFavoriteUseCase.isMovieFavorite(movieId).collect { isFavorite ->
                _isFavorite.value = isFavorite
            }
        }
    }

    fun addMovieToFavorites(movieId: Int) {
        viewModelScope.launch {
            toggleFavoriteUseCase.addMovieToFavorites(movieId)
            checkIfMovieIsFavorite(movieId)
        }
    }

    fun removeMovieFromFavorites(movieId: Int) {
        viewModelScope.launch {
            toggleFavoriteUseCase.removeMovieFromFavorites(movieId)
            checkIfMovieIsFavorite(movieId)
            loadFavoriteMovies()
        }
    }

    fun loadFavoriteMovies() {
        viewModelScope.launch {
            _isLoading.value = true
            val movieIds = toggleFavoriteUseCase.getAllFavoriteMovieIds()
            val movies = mutableListOf<MovieDetail>()
            for (id in movieIds) {
                getMovieDetailsUseCase(id).collect { resource ->
                    if (resource is Resource.Success) {
                        resource.data?.let { movies.add(it) }
                    }
                }
            }
            _favoriteMovies.value = movies
            _isLoading.value = false
        }
    }
}
