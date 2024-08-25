package com.srcython.cineway.presentation.ui.movie.moviedetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.srcython.cineway.domain.model.CastMember
import com.srcython.cineway.domain.model.MovieDetail
import com.srcython.cineway.domain.model.Review
import com.srcython.cineway.domain.usecase.GetMovieCreditsUseCase
import com.srcython.cineway.domain.usecase.GetMovieDetailsUseCase
import com.srcython.cineway.domain.usecase.GetMovieReviewsUseCase
import com.srcython.cineway.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailViewModel @Inject constructor(
    private val getMovieDetailsUseCase: GetMovieDetailsUseCase,
    private val getMovieReviewsUseCase: GetMovieReviewsUseCase,
    private val getMovieCreditsUseCase: GetMovieCreditsUseCase
) : ViewModel() {

    private val _movieDetail = MutableStateFlow<Resource<MovieDetail>>(Resource.Loading())
    val movieDetail: StateFlow<Resource<MovieDetail>> = _movieDetail

    private val _movieReviews = MutableStateFlow<Resource<List<Review>>>(Resource.Loading())
    val movieReviews: StateFlow<Resource<List<Review>>> = _movieReviews

    private val _movieCredits = MutableStateFlow<Resource<List<CastMember>>>(Resource.Loading())
    val movieCredits: StateFlow<Resource<List<CastMember>>> = _movieCredits

    fun fetchMovieDetails(movieId: Int) {
        viewModelScope.launch {
            getMovieDetailsUseCase(movieId).collect { result ->
                _movieDetail.value = result
            }
        }
    }

    fun fetchMovieReviews(movieId: Int) {
        viewModelScope.launch {
            getMovieReviewsUseCase(movieId).collect { result ->
                _movieReviews.value = result
            }
        }
    }

    fun fetchMovieCredits(movieId: Int) {
        viewModelScope.launch {
            getMovieCreditsUseCase(movieId).collect { result ->
                _movieCredits.value = result
            }
        }
    }
}
