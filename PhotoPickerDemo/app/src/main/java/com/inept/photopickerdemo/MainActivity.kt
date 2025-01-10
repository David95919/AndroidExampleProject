package com.inept.photopickerdemo

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.inept.photopickerdemo.ui.theme.PhotoPickerDemoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PhotoPickerDemoTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    PhotoPickerApp(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun PhotoPickerApp(modifier: Modifier = Modifier) {
    MultiPhotoPickerSample(modifier = modifier)
}

@Composable
fun PhotoPickerSample(modifier: Modifier = Modifier) {
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        selectedImageUri = uri
    }

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(onClick = { launcher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)) }) {
            Text("选择图片")
        }

        selectedImageUri?.let { uri ->
            Image(
                painter = rememberImagePainter(data = uri),
                contentDescription = null,
                modifier = Modifier
                    .size(200.dp)
            )
        }
    }
}

// MultiPhotoPickerSample 组合函数，用于展示如何在 Jetpack Compose 中实现多张图片或视频的选择器
@Composable
fun MultiPhotoPickerSample(modifier: Modifier = Modifier) {
    // 定义一个状态变量 selectedImages，用于存储用户选择的图片或视频的 URI 列表
    var selectedImages by remember { mutableStateOf<List<Uri>>(emptyList()) }

    // 定义一个 ActivityResultLauncher，用于启动多媒体选择器
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia() // 使用 PickMultipleVisualMedia 合约来选择多个图片或视频
    ) { uris ->
        // 当用户完成选择后，返回的 URI 列表将赋值给 selectedImages
        selectedImages = uris
    }

    // 使用 Column 布局来排列按钮和图片列表
    Column(
        modifier = modifier.fillMaxSize(), // 设置布局占满整个可用空间
        horizontalAlignment = Alignment.CenterHorizontally, // 水平居中对齐
        verticalArrangement = Arrangement.Center // 垂直居中排列
    ) {
        // 创建一个按钮，当用户点击时，启动媒体选择器
        Button(onClick = { launcher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageAndVideo)) }) {
            Text("选择多张图片") // 按钮上的文本
        }

        // 使用 LazyColumn 显示用户选择的图片列表
        LazyColumn {
            items(selectedImages) { uri ->
                // 使用 Image 组件来显示选中的图片
                Image(
                    painter = rememberImagePainter(data = uri), // 加载图片的 URI
                    contentDescription = null, // 图片的内容描述，这里设置为 null
                    modifier = Modifier
                        .size(200.dp) // 设置图片的大小为 200dp
                )
            }
        }
    }
}

