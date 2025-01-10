## 全局SnackBar
https://www.youtube.com/watch?v=KFazs62lIkE

## 1.导入需要的依赖
`libs.versions.toml`
```toml
[versions]
#....
navigationCompose = "2.8.5"
kotlinSerialization = "1.7.1"


[libraries]
#....
androidx-compose-navigation = { group = "androidx.navigation", name = "navigation-compose", version.ref= "navigationCompose"}
kotlinx-serialization-json = { group = "org.jetbrains.kotlinx", name = "kotlinx-serialization-json", version.ref = "kotlinSerialization" }
androidx-lifecycle-runtime-compose = { group = "androidx.lifecycle", name = "lifecycle-runtime-compose", version.ref = "lifecycleRuntimeKtx" }


[plugins]
#....
jetbrains-kotlin-serialization = { id = "org.jetbrains.kotlin.plugin.serialization", version.ref = "kotlin" }
```

`build.gradle.kts`
```kotlin
plugins {
    //...
    alias(libs.plugins.jetbrains.kotlin.serialization)
}

android {
    //...
}

dependencies {
    //...
    implementation(libs.androidx.compose.navigation)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.androidx.lifecycle.runtime.compose)
}
```

## 创建SnackbarController
`SnackbarController.kt`

```kotlin
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

data class SnackbarEvent(
    val message: String,
    val action: SnackbarAction? = null
)

data class SnackbarAction(
    val name: String,
    val action: suspend () -> Unit
)

object SnackbarController {
    private val _events = Channel<SnackbarEvent>()
    val events = _events.receiveAsFlow()

    suspend fun sendEvent(event: SnackbarEvent) {
        _events.send(event)
    }
}
```

## 创建ObserveAsEvents
`ObserveAsEvents.kt`

```kotlin
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

@Composable
fun <T> ObserveAsEvents(
    flow: Flow<T>,
    key1: Any? = null,
    key2: Any? = null,
    onEvent: (T) -> Unit
) {
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(lifecycleOwner.lifecycle, key1, key2, flow) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            withContext(Dispatchers.Main.immediate) {
                flow.collect(onEvent)
            }
        }
    }
}
```

## 修改MainActivity
`MainActivity.kt`
在 `onCreate` 添加
```kotlin
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

Scaffold(
    snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
) 
```