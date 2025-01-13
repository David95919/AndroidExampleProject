package com.inept.jetpackcomposenavigationdemo.ui.components

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.inept.jetpackcomposenavigationdemo.NavigationDestinations
import com.inept.jetpackcomposenavigationdemo.navigationDestinationsList

@Composable
fun NavigationBottomRow(onSelected: (NavigationDestinations) -> Unit, currentScreen: NavigationDestinations) {
    NavigationBar {
        navigationDestinationsList.forEach {
            NavigationBarItem(
                icon = { Icon(contentDescription = null, imageVector = it.icon) },
                label = { Text(it.route.capitalize()) },
                onClick = { onSelected(it) },
                selected = currentScreen == it
            )
        }
    }
}