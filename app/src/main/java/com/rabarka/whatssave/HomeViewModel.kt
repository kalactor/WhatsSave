package com.rabarka.whatssave

import android.os.Environment
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.rabarka.whatssave.data.Status
import com.rabarka.whatssave.data.WhatUiState
import java.io.File


class HomeViewModel : ViewModel() {

    var whatUiState by mutableStateOf(WhatUiState())
        private set

    init {
        getDire()
    }

    private fun getDire() {
        val imageListDir: MutableList<Status> = mutableListOf()
        val videoListDir: MutableList<Status> = mutableListOf()
        val file = File(STATUS_DIRECTORY.toString())
        if (file.exists()) {
            val listOfFiles = file.listFiles()
            if (listOfFiles != null) {
                for (e in listOfFiles) {
                    if (e.absolutePath.endsWith(".jpg")) {
                        imageListDir.add(Status(name = e.name, path = e.absolutePath))

                    }
                    if (e.absolutePath.endsWith(".mp4")) {
                        videoListDir.add(Status(name = e.name, path = e.absolutePath))

                    }
                }
            }
        }
        whatUiState = WhatUiState(imageListDir, videoListDir)
    }

    fun checkMyFolder() {
        val path = Constants.MY_APP_FOLDER_PATH
        val dir = File(path)
        var isDirectoryCreated = dir.exists()
        if (!isDirectoryCreated) {
            isDirectoryCreated = dir.mkdir()
        }
    }


    companion object {
        private val STATUS_DIRECTORY = File(
            Environment.getExternalStorageDirectory()
                .toString() + File.separator + "Android/media/com.whatsapp/WhatsApp/Media/.Statuses"
        )
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                HomeViewModel()
            }
        }
    }
}