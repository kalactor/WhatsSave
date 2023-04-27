package com.rabarka.whatssave.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import coil.ComponentRegistry
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.decode.VideoFrameDecoder
import coil.request.ImageRequest
import coil.request.videoFrameMillis
import coil.size.Size
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.ui.StyledPlayerView
import com.rabarka.whatssave.Constants
import com.rabarka.whatssave.HomeViewModel
import com.rabarka.whatssave.R
import org.w3c.dom.Text
import java.io.File
import java.io.FileDescriptor
import java.io.FileInputStream
import java.io.IOException

@Composable
fun ImageScreen(viewModel: HomeViewModel, onCardClick: (String) -> Unit) {
    DefaultScreen(
        list = viewModel.whatUiState.imageList,
        onCardClick = onCardClick
    ) { imagePath ->
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current).data(imagePath).crossfade(true)
                .build(),
            contentScale = ContentScale.FillBounds,
            contentDescription = "status image",
            placeholder = painterResource(id = R.drawable.loading_img)
        )
    }
}


@Composable
fun VideoScreen(viewModel: HomeViewModel, onCardClick: (String) -> Unit) {
    val context = LocalContext.current
    DefaultScreen(list = viewModel.whatUiState.videoList, onCardClick = onCardClick) { videoPath ->
        Image(
            painter = rememberAsyncImagePainter(
                model = ImageRequest.Builder(context).data(videoPath).videoFrameMillis(1000)
                    .crossfade(true).size(Size.ORIGINAL).placeholder(R.drawable.loading_img)
                    .build(),
                imageLoader = ImageLoader.Builder(context)
                    .components(fun ComponentRegistry.Builder.() { add(VideoFrameDecoder.Factory()) })
                    .build()
            ), contentDescription = "video thumbnail", contentScale = ContentScale.FillBounds
        )
    }
}


@Composable
fun ImageShower(viewModel: HomeViewModel, imageUri: String = "") {
    val path = "${Constants.STATUS_FOLDER_PATH}$imageUri"
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(path)
                .crossfade(true).build(),
            placeholder = painterResource(id = R.drawable.loading_img),
            contentDescription = null,
            contentScale = ContentScale.Inside,
            modifier = Modifier
        )
        ShareAndSaveButton(
            viewModel = viewModel,
            modifier = Modifier.align(Alignment.BottomCenter),
            context = LocalContext.current,
            imagePath = path
        )
    }
}


@Composable
fun VideoShower(viewModel: HomeViewModel, videoUri: String = "") {
    val context = LocalContext.current
    val path = "${Constants.STATUS_FOLDER_PATH}$videoUri"
    val exoPlayer = ExoPlayer.Builder(context).build().apply {
        setMediaItem(MediaItem.fromUri(path))
        prepare()
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        DisposableEffect(
            AndroidView(factory = { context ->
                StyledPlayerView(context).apply {
                    setShowNextButton(false)
                    setShowPreviousButton(false)
                    exoPlayer.playWhenReady = true
                    player = exoPlayer
                }
            })
        ) {
            onDispose {
                exoPlayer.release()
            }
        }
        ShareAndSaveButton(
            viewModel = viewModel,
            modifier = Modifier.align(Alignment.BottomCenter),
            context = context,
            imagePath = path
        )
    }
}

@Composable
fun ShareAndSaveButton(
    viewModel: HomeViewModel,
    modifier: Modifier,
    context: Context,
    imagePath: String
) {
    val sendIntent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_STREAM, Uri.parse("file:/$imagePath"))
        type = "image/jpeg"
    }
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        IconButton(onClick = {
            viewModel.checkMyFolder()
            val file = File(imagePath)
            val desPath = Constants.MY_APP_FOLDER_PATH
            val destFile = File(desPath)
            try {

            } catch (e: IOException) {
                e.printStackTrace()
            }

        }) {
            Icon(painter = painterResource(id = R.drawable.ic_download), contentDescription = null)
        }
        IconButton(onClick = {
            context.startActivity(Intent.createChooser(sendIntent, null))
        }) {
            Icon(painter = painterResource(id = R.drawable.ic_share), contentDescription = null)
        }
    }
}
