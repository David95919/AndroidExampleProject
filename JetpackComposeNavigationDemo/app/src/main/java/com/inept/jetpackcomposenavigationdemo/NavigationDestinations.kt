package com.inept.jetpackcomposenavigationdemo

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.FormatListNumbered
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink

interface NavigationDestinations {
    val icon: ImageVector
    val route: String
}

object Content : NavigationDestinations {
    override val icon = Icons.Filled.Apps
    override val route = "content"
}

object Download : NavigationDestinations {
    override val icon = Icons.Filled.Download
    override val route = "download"
}

object DownloadList : NavigationDestinations {
    override val icon = Icons.Filled.FormatListNumbered
    override val route = "download_list"
    const val downloadListArg = "download_list_string"
    val routeWithArgs = "${route}/{${downloadListArg}}"
    val arguments = listOf(
        navArgument(downloadListArg) { type = NavType.StringType }
    )
    val deepLinks = listOf(
        navDeepLink { uriPattern = "navigationdemo://$route/{$downloadListArg}"}
    )
}

val navigationDestinationsList = listOf(Content, Download)