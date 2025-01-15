# JetpackComposeNavigationDemo
JetpackComposeNavigation + NavigationBar展示

## Google官方Codelab
https://developer.android.com/codelabs/jetpack-compose-navigation

## 基础使用

### 1.添加依赖
```
implementation("androidx.navigation:navigation-compose:2.8.5")
implementation("androidx.compose.material:material-icons-extended") //可选 图标拓展
```

### 2.创建一个文件存储路由信息
创建一个接口并且单例对象实现,并且再创建一个列表存储单例对象

`NavigationDestinations.kt`

```kotlin
interface NavigationDestinations {
    val icon: ImageVector
    val route: String
}

object Content : NavigationDestinations {
    override val icon = Icons.Filled.Apps
    override val route = "content"
}

object Download : NavigationDestinations {
    override val icon = Icons.Filled.Download
    override val route = "download"
}

val navigationDestinationsList = listOf(Content, Download)
```

### 3.创建底部导航组件
`NavigationBottomRow.kt`

- `onSelected` 选择导航回调函数
- `currentScreen` 当前屏幕

```kotlin
@Composable
fun NavigationBottomRow(onSelected: (NavigationDestinations) -> Unit, currentScreen: NavigationDestinations) {
    NavigationBar {
        navigationDestinationsList.forEach {
            NavigationBarItem(
                icon = { Icon(contentDescription = null, imageVector = it.icon) },  //设置图标
                label = { Text(it.route.replaceFirstChar { char -> char.uppercase() }) },   //it.route首字母大写
                onClick = { onSelected(it) },
                selected = currentScreen == it
            )
        }
    }
}
```

### 4.创建NavHost
`MainActivity.kt`

- `navController` 路由控制器
- `startDestination` 起始界面路由

```kotlin
@Composable
fun NavigationNavHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Content.route,
    ) {
        composable(route = Content.route) {
            ContentScreen()
        }
        composable(Download.route) {
            DownloadScreen()
        }
    }
}
```

`ContentScreen.kt`
```kotlin
@Composable
fun ContentScreen() {
    Text("ContentScreen")
}
```

### 5.构建完整界面
`MainActivity.kt`

```kotlin
@Composable
fun NavigationApp(modifier: Modifier = Modifier) {
    //路由控制器
    val navController = rememberNavController()

    //获取当前屏幕
    val currentBackStack by navController.currentBackStackEntryAsState()
    val navDestination = currentBackStack?.destination
    var currentScreen =
        navigationDestinationsList.find { it.route == navDestination?.route } ?: Content

    Column(modifier = modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .weight(1f) //占满剩下空间
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

//方便使用的拓展函数
fun NavHostController.navigateSingleTopTo(route: String) {
    this.navigate(route = route) {
        popUpTo(id = this@navigateSingleTopTo.graph.findStartDestination().id) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}
```

![Screenshot_20250115-204035.png](image%2FScreenshot_20250115-204035.png)

## 实参导航
为了演示实参导航我们再构建一个界面为`DownloadList`

### 1.创建DownloadList界面与导航信息
再在`NavigationDestinations`创建一个`DownloadList`单列对象,再添加`downloadListArg`,`routeWithArgs`,`arguments`

- `routeWithArgs` 带参数的路由信息
- `arguments` 参数,type是参数类型

```kotlin
interface NavigationDestinations {
    val icon: ImageVector
    val route: String
}

//...

object DownloadList : NavigationDestinations {
    override val icon = Icons.Filled.FormatListNumbered
    override val route = "download_list"
    const val downloadListArg = "download_list_string"
    val routeWithArgs = "${route}/{${downloadListArg}}"
    val arguments = listOf(
        navArgument(downloadListArg) { type = NavType.StringType }
    )
}
```

`DownloadListScreen.kt`
```kotlin
@Composable
fun DownloadListScreen(downloadList: String?) {
    Column {
        Text("ListScreen")
        Text(downloadList ?: "Null")
    }
}
```

### 2.设置路由信息
route设置为DownloadList.routeWithArgs

```kotlin
@Composable
fun NavigationNavHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Content.route,
    ) {
        composable(route = Content.route) {
            ContentScreen()
        }
        composable(Download.route) {
            DownloadScreen()
        }

        composable(
            route = DownloadList.routeWithArgs,
            arguments = DownloadList.arguments,
        ) { navBackStackEntry ->
            val downloadList =
                navBackStackEntry.arguments?.getString(DownloadList.downloadListArg)
            DownloadListScreen(downloadList = downloadList)
        }
    }
}
```

### 3.如何使用
我们在`DownloadScreen`设置一个按钮跳转到`DownloadList`

- `onToDownloadList` 状态提升后进行导航

```kotlin
@Composable
fun DownloadScreen(onToDownloadList: () -> Unit) {
    Column {
        Text("DownloadScreen")
        Button(onClick = onToDownloadList) { Text("toDownloadList") }
    }
}
```

我们再调整`MainActivity`的`NavigationNavHost`
```kotlin
@Composable
fun NavigationNavHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Content.route,
    ) {
        //...
        composable(Download.route) {
            DownloadScreen(onToDownloadList = {
                navController.navigateToDownloadList(downloadList = "a,b,c,d,e,f")
            })
        }
        //...
}

//方便使用的拓展函数
private fun NavHostController.navigateToDownloadList(downloadList: String) {
    this.navigateSingleTopTo("${DownloadList.route}/$downloadList")
}
```

就可以接收参数了
![Screenshot_20250115-205642.png](image%2FScreenshot_20250115-205642.png)

## 深层链接
深层链接（Deep Link）是在应用内导航到特定页面的一种方式，可以通过外部链接（如浏览器、通知、其他App等）直接跳转到App中的某个页面。
在 Jetpack Compose Navigation 中，你可以为导航目标（NavGraph 的 Composable）设置深层链接，以便处理外部传入的URI或Intent。

### 1.设置`AndroidManifest.xml`

`scheme://download_list/传递的参数`
`navigationdemo://download_list/1,2,3,4,5`

```xml
<activity
    android:name=".MainActivity"
    android:exported="true"
    android:label="@string/app_name"
    android:theme="@style/Theme.JetpackComposeNavigationDemo">
    <intent-filter>
        <action android:name="android.intent.action.MAIN" />

        <category android:name="android.intent.category.LAUNCHER" />
    </intent-filter>

    <!--深层链接-->
    <intent-filter>
        <action android:name="android.intent.action.VIEW" />
        <category android:name="android.intent.category.DEFAULT" />
        <category android:name="android.intent.category.BROWSABLE" />
        <data android:scheme="navigationdemo" android:host="download_list" />
    </intent-filter>
</activity>
```

### 2.添加深层链接

在`NavigationDestinations`添加`deepLinks`,`deepLinks`与`AndroidManifest`设置的保持一致
```kotlin
object DownloadList : NavigationDestinations {
    override val icon = Icons.Filled.FormatListNumbered
    override val route = "download_list"
    const val downloadListArg = "download_list_string"
    val routeWithArgs = "${route}/{${downloadListArg}}"
    val arguments = listOf(
        navArgument(downloadListArg) { type = NavType.StringType }
    )
    val deepLinks = listOf(
        navDeepLink { uriPattern = "navigationdemo://$route/{$downloadListArg}"}
    )
}
```

我们再在`NavigationNavHost`设置深层链接
```kotlin
//DeepLink and Argument 深层链接和参数
composable(
    route = DownloadList.routeWithArgs,
    arguments = DownloadList.arguments,
    deepLinks = DownloadList.deepLinks
) { navBackStackEntry ->
    val downloadList =
        navBackStackEntry.arguments?.getString(DownloadList.downloadListArg)
    DownloadListScreen(downloadList = downloadList)
}
```

### 3.触发深层链接

#### adb启动
```shell
adb shell am start -d "navigationdemo://download_list/1,2,3,4,5" -a android.intent.action.VIEW
```

#### 其他应用或当前应用Intent启动
```kotlin
val intent = Intent(Intent.ACTION_VIEW, Uri.parse("navigationdemo://download_list/1,2,3,4,5"))
startActivity(intent)
```

#### App Shortcuts 触发
App Shortcuts 是 Android 的快捷方式功能，允许用户通过长按 App 图标，快速导航到某个页面

1. 在 `AndroidManifest.xml` 中配置动态快捷方式
```xml
<activity android:name=".MainActivity">
    <meta-data
        android:name="android.app.shortcuts"
        android:resource="@xml/shortcuts" />
</activity>
```

2. 在 `res/xml/shortcuts.xml` 文件中定义快捷方式
```xml
<?xml version="1.0" encoding="utf-8"?>
<shortcuts xmlns:android="http://schemas.android.com/apk/res/android">
    <shortcut
        android:shortcutId="details"
        android:enabled="true"
        android:icon="@drawable/ic_launcher_foreground"
        android:shortcutShortLabel="@string/shortcut_details_short_label"
        android:shortcutLongLabel="@string/shortcut_details_long_label">
        <intent
            android:action="android.intent.action.VIEW"
            android:data="navigationdemo://download_list/1,2,3,4,5" />
    </shortcut>
</shortcuts>
```

3. 在`res/values/strings.xml`设置字符串
```xml
<resources>
    <string name="app_name">Jetpack Compose Navigation Demo</string>
    <string name="shortcut_details_long_label">Open Details</string>
    <string name="shortcut_details_short_label">Details</string>
</resources>
```