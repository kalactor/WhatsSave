package com.rabarka.whatssave

import android.os.Environment
import com.rabarka.whatssave.data.BottomNavItem

object Constants {
    val BottomNavItems = listOf(
        BottomNavItem(
            label = "Image",
            route = "image"
        ),
        BottomNavItem(
            label = "Video",
            route = "video"
        )
    )

    const val STATUS_FOLDER_PATH = "/storage/emulated/0/Android/media/com.whatsapp/WhatsApp/Media/.Statuses/"

     val MY_APP_FOLDER_PATH = "${Environment.getExternalStorageDirectory().absolutePath}WhatsappStatus"

}