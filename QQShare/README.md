## 适配qq微信打开该应用
```xml
<activity
    android:name=".ShareReceiverActivity"
    android:exported="true"
    android:label="提示内容">
    <intent-filter>
        <action android:name="android.intent.action.VIEW" />
        <category android:name="android.intent.category.DEFAULT" />

        <data android:scheme="file" />
        <data android:scheme="content" />
        <data android:mimeType="*/*" />
    </intent-filter>
</activity>
```

自行修改mime类型