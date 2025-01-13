package com.inept.jetpackcomposenavigationdemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.inept.jetpackcomposenavigationdemo.ui.components.NavigationBottomRow
import com.inept.jetpackcomposenavigationdemo.ui.content.ContentScreen
import com.inept.jetpackcomposenavigationdemo.ui.download.DownloadScreen
import com.inept.jetpackcomposenavigationdemo.ui.list.ListScreen
import com.inept.jetpackcomposenavigationdemo.ui.theme.JetpackComposeNavigationDemoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            JetpackComposeNavigationDemoTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                ) { innerPadding ->
                    NavigationApp(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Preview
@Composable
fun NavigationApp(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    //获取当前屏幕
    val currentBackStack by navController.currentBackStackEntryAsState()
    val navDestination = currentBackStack?.destination
    var currentScreen =
        navigationDestinationsList.find { it.route == navDestination?.route } ?: Content

    println(currentScreen)

    Column(modifier = modifier.fillMaxSize()) {

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            NavigationNavHost(navController)
        }

        NavigationBottomRow(
            onSelected = { newScreen ->
                navController.navigateSingleTopTo(newScreen.route)
            },
            currentScreen = currentScreen
        )
    }
}

@Composable
fun NavigationNavHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Content.route,
    ) {
        composable(route = Content.route) {
            ContentScreen()
        }

        composable(route = Download.route) {
            DownloadScreen()
        }

        composable(route = List.route) {
            ListScreen()
        }
    }
}

fun NavHostController.navigateSingleTopTo(route: String) {
    this.navigate(route = route) {
        popUpTo(id = this@navigateSingleTopTo.graph.findStartDestination().id) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}