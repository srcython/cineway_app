package com.srcython.cineway.presentation.ui.movie.moviedetail

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.google.accompanist.pager.*
import com.srcython.cineway.R
import com.srcython.cineway.domain.model.CastMember
import com.srcython.cineway.domain.model.Review
import com.srcython.cineway.utils.Resource
import com.srcython.cineway.utils.extensions.toSpannedText
import kotlinx.coroutines.launch

@Composable
fun ViewPagerScreen(movieId: Int, viewModel: MovieDetailViewModel = hiltViewModel()) {
    val pages = listOf("About Movie", "Reviews", "Cast")
    val pagerState = rememberPagerState()
    val scope = rememberCoroutineScope()

    LaunchedEffect(movieId) {
        if (viewModel.movieDetail.value !is Resource.Success) {
            viewModel.fetchMovieDetails(movieId)
        }
        if (viewModel.movieReviews.value !is Resource.Success) {
            viewModel.fetchMovieReviews(movieId)
        }
        if (viewModel.movieCredits.value !is Resource.Success) {
            viewModel.fetchMovieCredits(movieId)
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        TabRow(selectedTabIndex = pagerState.currentPage) {
            pages.forEachIndexed { index, title ->
                Tab(
                    selected = pagerState.currentPage == index,
                    onClick = {
                        scope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                    },
                    text = { Text(text = title) }
                )
            }
        }

        HorizontalPager(
            count = pages.size,
            state = pagerState,
        ) { page ->
            when (page) {
                0 -> AboutMoviePage(viewModel)
                1 -> ReviewsPage(viewModel)
                2 -> CastPage()
            }
        }
    }
}


@Composable
fun AboutMoviePage(viewModel: MovieDetailViewModel) {
    val movieDetailState = viewModel.movieDetail.collectAsState()

    Column(
        modifier = Modifier
            .padding(start = 16.dp, end = 16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        when (val state = movieDetailState.value) {
            is Resource.Loading -> {
                Text(text = "Loading movie details...")
            }

            is Resource.Success -> {
                Text(
                    text = "Overview",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 8.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = state.data?.overview ?: "No overview available.",
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            is Resource.Error -> {
                Text(text = "Error: ${state.message}")
            }
        }
    }
}

@Composable
fun ReviewsPage(viewModel: MovieDetailViewModel) {
    val reviewsState = viewModel.movieReviews.collectAsState()

    Column(
        modifier = Modifier
            .padding(start = 16.dp, end = 16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        when (val state = reviewsState.value) {
            is Resource.Loading -> {
                Text(text = "Loading reviews...")
            }

            is Resource.Success -> {
                val reviews = state.data
                if (reviews.isNullOrEmpty()) {
                    Text(text = "No reviews available.")
                } else {
                    reviews.forEach { review ->
                        ReviewItem(review)
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }

            is Resource.Error -> {
                Text(text = "Error: ${state.message}")
            }
        }
    }
}

@Composable
fun ReviewItem(review: Review) {
    Row(modifier = Modifier.padding(8.dp)) {
        Image(
            painter = painterResource(id = R.drawable.ic_review),
            contentDescription = "Review Icon",
            modifier = Modifier
                .size(24.dp)
                .padding(end = 5.dp)
        )

        Column {
            Text(
                text = review.author ?: "Unknown Author",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))

            BasicText(
                text = buildAnnotatedString {
                    append(AnnotatedString(review.content?.toSpannedText().toString()))
                },
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@Composable
fun CastPage(viewModel: MovieDetailViewModel = hiltViewModel()) {
    val movieCreditsState = viewModel.movieCredits.collectAsState()

    when (val state = movieCreditsState.value) {
        is Resource.Loading -> {
            Text(text = "Loading cast...")
        }

        is Resource.Success -> {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.padding(top = 16.dp, start = 16.dp, end = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(state.data ?: emptyList()) { castMember ->
                    CastItem(castMember)
                }
            }
        }

        is Resource.Error -> {
            Text(text = "Error: ${state.message}")
        }
    }
}

@Composable
fun CastItem(castMember: CastMember) {
    Column(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val profileImage = castMember.profilePath?.let {
            "https://image.tmdb.org/t/p/w500$it"
        } ?: "https://via.placeholder.com/100x150"

        Image(
            painter = rememberAsyncImagePainter(model = profileImage),
            contentDescription = castMember.name,
            modifier = Modifier
                .size(120.dp, 180.dp)
                .clip(RoundedCornerShape(8.dp))
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = castMember.name,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 4.dp)
        )

        val nicknames = castMember.character.split("/")
        nicknames.forEach { nickname ->
            Text(
                text = nickname.trim(),
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 2.dp)
            )
        }
    }
}
