package com.inept.jetpackcomposenavigationdemo.ui.downloadList

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun DownloadListScreen(downloadList: String?) {
    Column {
        Text("ListScreen")
        Text(downloadList ?: "Null")
    }
}