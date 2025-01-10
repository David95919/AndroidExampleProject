package com.inept.snackbardemo

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

// 用于描述 SnackBar 消息和可选的操作按钮
data class SnackbarEvent(
    val message: String, // SnackBar 要显示的消息
    val action: SnackbarAction? = null // 可选的按钮操作，默认是 null
)

// 描述 SnackBar 按钮的动作
data class SnackbarAction(
    val name: String, // 按钮名称
    val action: suspend () -> Unit // 按钮点击时执行的操作
)

// 控制 SnackBar 事件的对象
object SnackbarController {
    // 私有的 Channel 用于传递 Snackbar 事件
    private val _events = Channel<SnackbarEvent>()

    // 将 Channel 转换为 Flow，外部只能读取事件流，不能直接发送事件
    val events = _events.receiveAsFlow()

    // 向 Channel 发送新的 Snackbar 事件
    suspend fun sendEvent(event: SnackbarEvent) {
        _events.send(event) // 将事件发送到 Channel
    }
}
