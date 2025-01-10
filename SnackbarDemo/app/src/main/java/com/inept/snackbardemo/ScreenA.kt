package com.inept.snackbardemo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch

@Composable
fun ScreenA(
    modifier: Modifier = Modifier,
    viewModel: ScreenAViewModel = viewModel(),
    onNavigator: () -> Unit
) {
    val scope = rememberCoroutineScope()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(onClick = {
            scope.launch { SnackbarController.sendEvent(SnackbarEvent("这是一条消息")) }
        }) {
            Text("显示一条消息")
        }

        Button(onClick = {
            viewModel.showSnackbar()
        }) {
            Text("在viewmodel触发")
        }

        Button(onClick = onNavigator) {
            Text("导航")
        }
    }
}