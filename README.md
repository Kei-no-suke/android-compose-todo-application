# android-compose-todo-application
このアプリはTodoリストです。初めて作成したAndroidアプリです。

## アプリ配信
[Google Play Store](https://play.google.com/store/apps/details?id=com.keinosuke.todoapplication)

>[!CAUTION]
>このコードにはリリース前の変更も含まれています。  

## 想定環境
使用言語: Kotlin  
Android Studio Hedgehog | 2023.1.1  
OS: Windows 11  
minSdkVersion: 26  
targetSdkVersion: 33  

## アプリのメイン画面

## 利用手順


## 使用ライブラリ
### Room
build.gradle(Module)
```
plugins {
    id 'com.google.devtools.ksp' version "1.8.21-1.0.11"
}

dependencies {
    def room_version = "2.5.0"
    implementation "androidx.room:room-runtime:$room_version"
    annotationProcessor "androidx.room:room-compiler:$room_version"
    ksp "androidx.room:room-compiler:$room_version"
    implementation "androidx.room:room-ktx:$room_version"
}
```
### ViewModel
build.gradle(Module)
```
dependencies {
    implementation "androidx.lifecycle:lifecycle-viewmodel-compose:2.5.1"
    implementation "androidx.lifecycle:lifecycle-viewmodel-ktx:2.5.1"
    implementation "androidx.lifecycle:lifecycle-viewmodel-savedstate:2.5.1"
}
```
### Navigation
build.gradle(Module)
```
dependencies {
    def nav_version = "2.5.3"
    implementation "androidx.navigation:navigation-fragment-ktx:$nav_version"
    implementation "androidx.navigation:navigation-ui-ktx:$nav_version"
    implementation "androidx.navigation:navigation-compose:$nav_version"
}
```

## 使用権限
このアプリで使用する権限はありません。
