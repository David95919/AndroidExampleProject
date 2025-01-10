package com.inept.snackbardemo

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

// 定义一个泛型函数，ObserveAsEvents，用于观察 Flow 中的事件
@Composable
fun <T> ObserveAsEvents(
    flow: Flow<T>, // 要观察的 Flow
    key1: Any? = null, // 可选的键1，作为 LaunchedEffect 的依赖项
    key2: Any? = null, // 可选的键2，作为 LaunchedEffect 的依赖项
    onEvent: (T) -> Unit // 事件发生时的回调
) {
    // 获取当前生命周期拥有者（通常是当前 Composable 所在的 Activity 或 Fragment）
    val lifecycleOwner = LocalLifecycleOwner.current

    // 使用 LaunchedEffect 来启动协程，监听生命周期和 key1、key2、flow 的变化
    LaunchedEffect(lifecycleOwner.lifecycle, key1, key2, flow) {
        // 在生命周期处于 STARTED 状态时重复执行收集 Flow 的操作
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            // 在主线程中收集 Flow 的数据，并执行传入的 onEvent 回调
            withContext(Dispatchers.Main.immediate) {
                flow.collect(onEvent)
            }
        }
    }
}