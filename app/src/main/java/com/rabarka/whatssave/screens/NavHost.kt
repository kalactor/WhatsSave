package com.rabarka.whatssave.screens

import android.widget.Toast
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.rabarka.whatssave.Constants
import com.rabarka.whatssave.HomeViewModel
import com.rabarka.whatssave.R

@Composable
fun MyAppScreen() {
    val navHostController = rememberNavController()
    val homeViewModel: HomeViewModel = viewModel(factory = HomeViewModel.Factory)
    val context = LocalContext.current
    Scaffold(bottomBar = {
        MyBottomAppBar(navHostController = navHostController)
    }) { innerPadding ->
        NavHost(
            navController = navHostController,
            startDestination = "image",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("image") {
                ImageScreen(viewModel = homeViewModel) { path ->
                    navHostController.navigate(route = "imageShower/$path")
                }
            }
            composable("video") {
                VideoScreen(viewModel = homeViewModel) { path ->
                    navHostController.navigate("videoShower/$path")
                }
            }
            composable("imageShower/{uri}") {
                val imagePathUri = it.arguments?.getString("uri")
                if (imagePathUri != null) {
                    ImageShower(viewModel = homeViewModel, imageUri = imagePathUri)
                } else {
                    Toast.makeText(context, "Card Value is null", Toast.LENGTH_SHORT).show()
                }
            }
            composable("videoShower/{videoUri}") {
                val videoPathUri = it.arguments?.getString("videoUri")
                if (videoPathUri != null) {
                    VideoShower(viewModel = homeViewModel, videoUri = videoPathUri)
                } else {
                    Toast.makeText(context, "Card Value is null", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }
}


@Composable
fun MyBottomAppBar(navHostController: NavHostController) {
    NavigationBar {

        val navBackStackEntry by navHostController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        Constants.BottomNavItems.forEach {
            NavigationBarItem(
                selected = currentRoute == it.route,
                onClick = {
                    navHostController.navigate(it.route)
                },
                icon = {
                    if (it.label == "Image") {
                        if (currentRoute == it.route) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_image_filled),
                                contentDescription = null
                            )
                        } else {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_image_outlined),
                                contentDescription = null
                            )
                        }
                    } else {
                        if (currentRoute == it.route) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_video_filled),
                                contentDescription = null
                            )
                        } else {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_video_outlined),
                                contentDescription = null
                            )
                        }
                    }

                },
                label = {
                    Text(text = it.label)
                },
                alwaysShowLabel = false
            )
        }
    }
}