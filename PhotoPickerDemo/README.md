## PhotoPicker图片选择器
这是一个在安卓13推出的隐私安全的媒体选择器

## 图片与视频
```kotlin
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
        // 创建一个按钮，当用户点击时，启动媒体选择器,ImageAndVideo是图片与视频
        Button(onClick = { launcher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageAndVideo)) }) {
            Text("选择多张图片与视频") // 按钮上的文本
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
```