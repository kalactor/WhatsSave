package com.rabarka.whatssave.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rabarka.whatssave.data.Status

@Composable
fun DefaultScreen(
    modifier: Modifier = Modifier,
    list: List<Status>,
    onCardClick: (String) -> Unit,
    content: @Composable (String) -> Unit
) {
    ImageCardList(
        listOfDir = list,
        modifier = modifier,
        onCardClick = onCardClick,
        content = content
    )
}


@Composable
fun ImageCardList(
    listOfDir: List<Status>,
    modifier: Modifier = Modifier,
    onCardClick: (String) -> Unit,
    content: @Composable (String) -> Unit
) {
    LazyVerticalGrid(columns = GridCells.Adaptive(minSize = 192.dp)) {
        items(listOfDir) {
            ImageCard(
                imagePath = it,
                modifier = modifier,
                onCardClick = onCardClick,
                content = content
            )
        }
    }
}

@Composable
fun ImageCard(
    imagePath: Status,
    modifier: Modifier = Modifier,
    onCardClick: (String) -> Unit,
    content: @Composable (String) -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .padding(4.dp)
            .clickable(onClick = {
                onCardClick(imagePath.name)
            }),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        content(imagePath.path)
    }
}