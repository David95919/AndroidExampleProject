package com.inept.jetpackcomposenavigationdemo

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.FormatListNumbered
import androidx.compose.ui.graphics.vector.ImageVector

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

object List : NavigationDestinations {
    override val icon = Icons.Filled.FormatListNumbered
    override val route = "list"
}

val navigationDestinationsList = listOf(Content, Download, List)