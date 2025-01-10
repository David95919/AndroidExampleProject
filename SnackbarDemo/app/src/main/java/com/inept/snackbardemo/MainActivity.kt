package com.inept.snackbardemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.inept.snackbardemo.ui.theme.SnackbarDemoTheme
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

// 用于描述屏幕A的对象
@Serializable
data object ScreenA

// 用于描述屏幕B的对象
@Serializable
data object ScreenB

// MainActivity 负责显示 UI 和控制 Snackbar 事件
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() // 启用边缘到边缘显示，适配全屏幕显示
        setContent {
            SnackbarDemoTheme {
                // 初始化导航控制器
                val navController = rememberNavController()
                // 创建 Snackbar 的 HostState，用于管理 Snackbar 的状态
                val snackbarHostState = remember { SnackbarHostState() }
                // 创建一个协程作用域，控制异步任务
                val scope = rememberCoroutineScope()

                // 使用 ObserveAsEvents 监听 SnackbarController 中的事件
                ObserveAsEvents(
                    flow = SnackbarController.events, // 监听 Snackbar 的事件流
                    key1 = snackbarHostState // 依赖项，确保 Snackbar 状态变化时重新执行
                ) { snackBarEvent ->
                    // 在协程中处理事件
                    scope.launch {
                        // 如果当前有显示的 Snackbar，先将其关闭
                        snackbarHostState.currentSnackbarData?.dismiss()

                        // 显示新的 Snackbar
                        val result = snackbarHostState.showSnackbar(
                            message = snackBarEvent.message, // 消息内容
                            actionLabel = snackBarEvent.action?.name, // 可选的操作按钮名称
                            duration = SnackbarDuration.Long // Snackbar 显示时间
                        )

                        // 如果用户点击了操作按钮，执行相应的操作
                        if (result == SnackbarResult.ActionPerformed) {
                            snackBarEvent.action?.action?.invoke()
                        }
                    }
                }

                // 使用 Scaffold 布局，包含一个 SnackbarHost 用于显示 Snackbar
                Scaffold(
                    modifier = Modifier.fillMaxSize(), // 填满整个屏幕
                    snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
                ) { innerPadding ->

                    // 设置导航主机，处理不同屏幕之间的切换
                    NavHost(
                        navController = navController,
                        startDestination = ScreenA,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable<ScreenA> {
                            ScreenA(onNavigator = {
                                navController.navigate(ScreenB)
                            })
                        }
                        composable<ScreenB> {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("this is ScreenB")
                            }
                        }
                    }
                }
            }
        }
    }
}
