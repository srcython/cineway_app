package com.srcython.cineway.presentation.ui.movie.movielist

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
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
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.rememberAsyncImagePainter
import com.srcython.cineway.R
import com.srcython.cineway.domain.model.Genre
import com.srcython.cineway.domain.model.Movie
import com.srcython.cineway.utils.Resource
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieListScreen(
    navController: NavController,
    viewModel: MovieListViewModel = hiltViewModel()
) {
    var isFilterDialogOpen by rememberSaveable { mutableStateOf(false) }
    var selectedGenres by rememberSaveable { mutableStateOf<List<Int>>(emptyList()) }
    var selectedSortOption by rememberSaveable { mutableStateOf("popularity.desc") }
    val searchQuery by viewModel.searchQuery.collectAsState()
    val movies = viewModel.movies.collectAsLazyPagingItems()
    val listState = rememberLazyGridState()
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 50.dp, start = 8.dp, end = 8.dp)
    ) {
        Card(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .padding(5.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "cineway",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(start = 12.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { viewModel.onSearchQueryChanged(it) },
                    modifier = Modifier
                        .height(55.dp)
                        .weight(1f),
                    placeholder = { Text(text = "Search movies...") },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { viewModel.onSearchQueryChanged("") }) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_clear),
                                    contentDescription = "Clear search"
                                )
                            }
                        } else {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_search),
                                contentDescription = "Search icon"
                            )
                        }
                    },
                    singleLine = true,
                    shape = MaterialTheme.shapes.large,
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = Color.Gray
                    )
                )

                IconButton(
                    onClick = { navController.navigate("favorite_movies") },
                    modifier = Modifier.padding(start = 8.dp)

                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_favorites),
                        contentDescription = "Filter icon",
                    )
                }

                IconButton(
                    onClick = { isFilterDialogOpen = true },
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_filter),
                        contentDescription = "Filter icon"
                    )
                }

            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        if (isFilterDialogOpen) {
            FilterDialog(
                selectedGenres = selectedGenres,
                genres = viewModel.genres.collectAsState().value,
                selectedSortOption = selectedSortOption,
                onSortBySelected = { sortBy -> selectedSortOption = sortBy },
                onGenresSelected = { genres -> selectedGenres = genres },
                onDismissRequest = { isFilterDialogOpen = false },
                onApply = {
                    viewModel.getFilteredMovies(
                        sortBy = selectedSortOption,
                        genres = selectedGenres
                    )
                    isFilterDialogOpen = false
                }
            )
        }

        Box(modifier = Modifier.fillMaxSize()) {
            when (movies.loadState.refresh) {
                is LoadState.Loading -> LoadingState()
                is LoadState.Error -> ErrorState("Error loading movies.")
                else -> MovieList(navController, movies, listState)
            }

            FloatingActionButton(
                onClick = { coroutineScope.launch { listState.animateScrollToItem(0) } },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp),
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_arrow),
                    contentDescription = "Scroll to top"
                )
            }
        }
    }
}

@Composable
fun LoadingState() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

@Composable
fun ErrorState(message: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = message, color = Color.Red)
    }
}

@Composable
fun MovieList(
    navController: NavController,
    movies: LazyPagingItems<Movie>,
    listState: LazyGridState
) {
    LazyVerticalGrid(
        state = listState,
        columns = GridCells.Fixed(2),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(top = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(movies.itemCount) { index ->
            val movie = movies[index]
            movie?.let {
                MovieItem(it) { navController.navigate("detail/${movie.id}") }
            }
        }
    }
}


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FilterDialog(
    selectedGenres: List<Int>,
    genres: Resource<List<Genre>>,
    selectedSortOption: String,
    onSortBySelected: (String) -> Unit,
    onGenresSelected: (List<Int>) -> Unit,
    onDismissRequest: () -> Unit,
    onApply: () -> Unit
) {
    var selectedOption by remember { mutableStateOf(selectedSortOption) }
    var selectedGenreIds by remember { mutableStateOf(selectedGenres) }

    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text(text = "Filter & Sort Options") },
        text = {
            Column {
                Text(text = "Sort by Popularity:")
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = selectedOption == "popularity.desc",
                        onClick = { selectedOption = "popularity.desc" }
                    )
                    Text(text = "Descending")

                    Spacer(modifier = Modifier.width(16.dp))

                    RadioButton(
                        selected = selectedOption == "popularity.asc",
                        onClick = { selectedOption = "popularity.asc" }
                    )
                    Text(text = "Ascending")
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(text = "Sort by Rating:")
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = selectedOption == "vote_average.desc",
                        onClick = { selectedOption = "vote_average.desc" }
                    )
                    Text(text = "Descending")

                    Spacer(modifier = Modifier.width(16.dp))

                    RadioButton(
                        selected = selectedOption == "vote_average.asc",
                        onClick = { selectedOption = "vote_average.asc" }
                    )
                    Text(text = "Ascending")
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(text = "Select Genres:")
                when (genres) {
                    is Resource.Success -> {
                        FlowRow(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            genres.data?.forEach { genre ->
                                GenreChip(
                                    genre = genre,
                                    selected = selectedGenreIds.contains(genre.id),
                                    onSelected = { selected ->
                                        selectedGenreIds = if (selected) {
                                            selectedGenreIds + genre.id
                                        } else {
                                            selectedGenreIds - genre.id
                                        }
                                    }
                                )
                            }
                        }
                    }

                    is Resource.Error -> {
                        Text(text = "Failed to load genres.")
                    }

                    is Resource.Loading -> {
                        CircularProgressIndicator()
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onGenresSelected(selectedGenreIds)
                    onSortBySelected(selectedOption)
                    onApply()
                }
            ) {
                Text("Apply")
            }
        },
        dismissButton = {
            Row {
                TextButton(onClick = {
                    selectedOption = "popularity.desc"
                    selectedGenreIds = emptyList()
                }) {
                    Text("Reset")
                }
                TextButton(onClick = onDismissRequest) {
                    Text("Cancel")
                }
            }
        }
    )
}

@Composable
fun GenreChip(
    genre: Genre,
    selected: Boolean,
    onSelected: (Boolean) -> Unit
) {
    val backgroundColor =
        if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface
    val borderColor = if (selected) MaterialTheme.colorScheme.primary else Color.Gray

    Box(
        modifier = Modifier
            .padding(4.dp)
            .clickable { onSelected(!selected) }
            .background(color = backgroundColor, shape = MaterialTheme.shapes.small)
            .border(1.dp, borderColor, shape = MaterialTheme.shapes.small)
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text = genre.name, color = if (selected) Color.White else Color.Black)
    }
}

@Composable
fun MovieItem(movie: Movie, onClick: () -> Unit) {
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
                movie.title?.let {
                    Column(
                        modifier = Modifier.padding(horizontal = 8.dp)
                    ) {
                        Text(
                            text = it,
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
                                Image(
                                    painter = painterResource(id = R.drawable.ic_date),
                                    contentDescription = "Icon",
                                    modifier = Modifier.size(18.dp)
                                )

                                Spacer(modifier = Modifier.width(2.dp))

                                movie.releaseDate?.let { it1 ->
                                    Text(
                                        text = it1.ifEmpty { "Unknown" },
                                        style = MaterialTheme.typography.bodySmall,
                                        color = Color.White,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                            }

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.End
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.ic_star),
                                    contentDescription = "Icon",
                                    modifier = Modifier.size(18.dp)
                                )

                                Spacer(modifier = Modifier.width(2.dp))

                                Text(
                                    text = movie.voteAverage.toString().ifEmpty { "Unknown" },
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
            }
        }
    }
}
