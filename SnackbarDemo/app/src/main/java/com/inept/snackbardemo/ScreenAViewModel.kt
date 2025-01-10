package com.inept.snackbardemo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

// ScreenA 的 ViewModel，用于处理界面上的逻辑和 Snackbar 事件的发送
class ScreenAViewModel : ViewModel() {

    // 显示一个 Snackbar，包含消息和一个按钮操作
    fun showSnackbar() {
        // 在 ViewModel 的作用域中启动一个协程
        viewModelScope.launch {
            // 发送一个 Snackbar 事件，触发 SnackBar 显示
            SnackbarController.sendEvent(
                event = SnackbarEvent(
                    message = "ViewModel调用Snackbar", // Snackbar 显示的消息
                    action = SnackbarAction(
                        name = "点击触发Action", // 按钮名称
                        action = {
                            // 当用户点击按钮时，发送另一个 Snackbar 事件
                            SnackbarController.sendEvent(
                                event = SnackbarEvent(
                                    message = "点击了Action" // 点击后显示的消息
                                )
                            )
                        }
                    )
                )
            )
        }
    }
}