package com.inept.jetpackcomposenavigationdemo.ui.download

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun DownloadScreen(onToDownloadList: () -> Unit) {
    Column {
        Text("DownloadScreen")
        Button(onClick = onToDownloadList) { Text("toDownloadList") }
    }
}