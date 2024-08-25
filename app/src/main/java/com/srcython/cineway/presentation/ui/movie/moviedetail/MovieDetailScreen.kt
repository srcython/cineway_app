package com.srcython.cineway.presentation.ui.movie.moviedetail

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.shape.ZeroCornerSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.srcython.cineway.R
import com.srcython.cineway.domain.model.MovieDetail
import com.srcython.cineway.presentation.ui.movie.favoritemovie.FavoriteMovieViewModel
import com.srcython.cineway.utils.Resource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrailerScreen(
    movieId: Int,
    navController: NavController,
    movieDetailViewModel: MovieDetailViewModel = hiltViewModel(),
    favoriteMovieViewModel: FavoriteMovieViewModel = hiltViewModel()
) {
    LaunchedEffect(movieId) {
        movieDetailViewModel.fetchMovieDetails(movieId)
        favoriteMovieViewModel.checkIfMovieIsFavorite(movieId)
    }

    val movieDetailState = movieDetailViewModel.movieDetail.collectAsState()
    val isFavorite = favoriteMovieViewModel.isFavorite.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Detail",
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                navigationIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_arrow_back),
                        contentDescription = "Back",
                        tint = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier
                            .clickable { navController.navigateUp() }
                            .padding(8.dp)
                    )
                },
                actions = {
                    Icon(
                        painter = painterResource(
                            id = if (isFavorite.value) R.drawable.ic_remove_favorites else R.drawable.ic_add_favorites
                        ),
                        contentDescription = "Toggle Favorite",
                        tint = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier
                            .clickable {
                                if (isFavorite.value) {
                                    favoriteMovieViewModel.removeMovieFromFavorites(movieId)
                                } else {
                                    favoriteMovieViewModel.addMovieToFavorites(movieId)
                                }
                            }
                            .size(28.dp)
                    )
                    Spacer(modifier = Modifier.width(25.dp))
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(),
            )
        },
        content = { paddingValues ->
            when (val state = movieDetailState.value) {
                is Resource.Loading -> {
                    LoadingState()
                }

                is Resource.Success -> {
                    SuccessState(
                        movie = state.data,
                        paddingValues = paddingValues,
                        movieId = movieId,
                        movieDetailViewModel = movieDetailViewModel
                    )
                }

                is Resource.Error -> {
                    ErrorState(message = state.message)
                }
            }
        }
    )
}

@Composable
fun LoadingState() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

@Composable
fun SuccessState(
    movie: MovieDetail?,
    paddingValues: PaddingValues,
    movieId: Int,
    movieDetailViewModel: MovieDetailViewModel
) {
    val posterUrl = movie?.posterPath?.let {
        "https://image.tmdb.org/t/p/w500$it"
    } ?: "https://via.placeholder.com/500x750"

    val backdropUrl = movie?.backdropPath?.let {
        "https://image.tmdb.org/t/p/w500$it"
    } ?: "https://via.placeholder.com/1280x720"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(280.dp)
        ) {

            val painter = rememberAsyncImagePainter(
                model = backdropUrl
            )
            Image(
                painter = painter,
                contentDescription = "Backdrop Image",
                modifier = Modifier
                    .fillMaxSize()
                    .clip(
                        shape = MaterialTheme.shapes.medium.copy(
                            topStart = ZeroCornerSize,
                            topEnd = ZeroCornerSize,
                            bottomEnd = CornerSize(15.dp),
                            bottomStart = CornerSize(15.dp)
                        )
                    ),
                contentScale = ContentScale.Crop
            )

            val posterPainter = rememberAsyncImagePainter(
                model = posterUrl
            )
            Image(
                painter = posterPainter,
                contentDescription = "Overlay Image",
                modifier = Modifier
                    .padding(start = 16.dp, top = 16.dp)
                    .size(120.dp, 180.dp)
                    .offset(y = 160.dp)
                    .clip(RoundedCornerShape(15.dp))
            )
        }

        Text(
            text = movie?.title ?: "Movie Title",
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold,
                lineHeight = 22.sp
            ),
            modifier = Modifier
                .padding(top = 15.dp, start = 150.dp, end = 16.dp)
                .fillMaxWidth(),
            color = Color(0xFF242A32),
            fontSize = 20.sp,
            fontFamily = FontFamily.Serif
        )

        Spacer(modifier = Modifier.height(45.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .align(Alignment.CenterHorizontally)
        ) {
            Icon(
                painterResource(id = R.drawable.ic_date),
                contentDescription = "Release Date",
                tint = Color.Black
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = movie?.releaseDate ?: "N/A",
                fontSize = 12.sp,
                color = Color.Black
            )
            Text(
                text = " | ",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = Color.Black
            )
            Icon(
                painterResource(id = R.drawable.ic_clock),
                contentDescription = "Duration",
                tint = Color.Black
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "${movie?.runtime ?: 0} min",
                fontSize = 12.sp,
                color = Color.Black
            )
            Text(
                text = " | ",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = Color.Black
            )
            Icon(
                painterResource(id = R.drawable.ic_ticket),
                contentDescription = "Genre",
                tint = Color.Black
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = movie?.genres?.firstOrNull() ?: "N/A",
                fontSize = 12.sp,
                color = Color.Black
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        ViewPagerScreen(movieId, movieDetailViewModel)
    }
}

@Composable
fun ErrorState(message: String?) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(
            text = "Error: ${message ?: "Unknown error"}",
            color = Color.Red
        )
    }
}
