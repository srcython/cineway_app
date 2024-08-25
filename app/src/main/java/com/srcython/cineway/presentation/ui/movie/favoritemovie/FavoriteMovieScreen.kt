package com.srcython.cineway.presentation.ui.movie.favoritemovie

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.srcython.cineway.domain.model.MovieDetail
import com.srcython.cineway.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoriteMovieScreen(
    navController: NavController,
    viewModel: FavoriteMovieViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) {
        viewModel.loadFavoriteMovies()
    }

    val favoriteMovies = viewModel.favoriteMovies.collectAsState().value
    val isLoading = viewModel.isLoading.collectAsState().value
    var movieToRemove by remember { mutableStateOf<MovieDetail?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(modifier = Modifier.weight(1f))
                        Text(text = "Favorite Movies")
                        Box(modifier = Modifier.weight(1f))
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_arrow_back),
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    Spacer(modifier = Modifier.width(40.dp))
                }
            )
        },
        content = { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                when {
                    isLoading -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }

                    favoriteMovies.isEmpty() -> {
                        Text(
                            text = "No favorite movies",
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }

                    else -> {
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(8.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(favoriteMovies.size) { index ->
                                val movie = favoriteMovies[index]
                                MovieItem(movie = movie, onClick = {
                                    navController.navigate("detail/${movie.id}")
                                }, onRemoveClick = {
                                    movieToRemove = movie
                                })
                            }
                        }
                    }
                }
            }

            movieToRemove?.let { movie ->
                AlertDialog(
                    onDismissRequest = { movieToRemove = null },
                    title = { Text(text = "Remove from Favorites?") },
                    text = { Text(text = "Are you sure you want to remove ${movie.title} from your favorites?") },
                    confirmButton = {
                        TextButton(onClick = {
                            viewModel.removeMovieFromFavorites(movie.id)
                            movieToRemove = null
                        }) {
                            Text("Yes")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { movieToRemove = null }) {
                            Text("No")
                        }
                    }
                )
            }
        }
    )
}


@Composable
fun MovieItem(movie: MovieDetail, onClick: () -> Unit, onRemoveClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            val posterUrl = movie.posterPath?.let {
                "https://image.tmdb.org/t/p/w500$it"
            } ?: "https://via.placeholder.com/500x750"

            Image(
                painter = rememberAsyncImagePainter(posterUrl),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(2f / 3.2f),
                contentScale = ContentScale.Crop
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .background(Color.Black.copy(alpha = 0.5f))
                    .padding(vertical = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 8.dp)
                ) {
                    Text(
                        text = movie.title ?: "Unknown",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Start
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_date),
                                contentDescription = "Release Date",
                                modifier = Modifier.size(18.dp),
                                tint = Color.White
                            )

                            Spacer(modifier = Modifier.width(2.dp))

                            Text(
                                text = movie.releaseDate?.ifEmpty { "Unknown" } ?: "Unknown",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.White,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.End
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_star),
                                contentDescription = "Rating",
                                modifier = Modifier.size(18.dp),
                                tint = Color(0xFFFFA500)
                            )

                            Spacer(modifier = Modifier.width(2.dp))

                            Text(
                                text = movie.voteAverage?.toString()?.ifEmpty { "N/A" } ?: "N/A",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                color = Color(0xFFFFA500),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }

            IconButton(
                onClick = onRemoveClick,
                modifier = Modifier
                    .align(Alignment.TopEnd)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_remove),
                    contentDescription = "Remove from Favorites",
                    tint = Color.White,
                    modifier = Modifier.size(30.dp)
                )
            }
        }
    }
}