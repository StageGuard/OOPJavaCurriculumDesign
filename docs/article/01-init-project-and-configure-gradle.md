### 初始化项目，配置 `gradle` 子项目

#### 整体项目结构：

```
OOPJavaCurriculumDesign
├─── backend  <---- 后端实现。
│   ├─── src
│   └─── ...
├─── frontend-android  <---- Android 移动前端实现。
│   ├─── src
│   └─── ...
├─── frontend-web  <---- Web 前端实现。
├─── frontend-desktop  <---- 桌面前端实现。
│   ├─── src
│   └─── ...
├─── docs  <---- 开发博客。
└─── ...
```

#### 子项目配置

在 [`OOPJavaCurriculumDesign\build.gradle`](../../build.gradle) 中：

```kotlin
allprojects {
    group 'me.stageguard.oopcd'
    version '0.1'

    repositories {
        google()
        jcenter()
        mavenCentral()
    }
}
```

`allProjects {}` 可以为所有子项目和本身配置共性的设置，如项目组 `group` 和项目版本 `version`。

```kotlin
buildscript {
    dependencies {
        classpath 'com.android.tools.build:gradle:4.1.3'
    }
}
```

由于 Android 前端是一个 Android 项目，所以在根项目 `buildscript {}` 的依赖中要添加 Android 构建工具。

在 [`OOPJavaCurriculumDesign\gradle.properties`](../../gradle.properties) 中：

```properties
android.useAndroidX=true
```

由于 Android 前端使用到了 Android X 库，所以要在这里标明。

在 [`OOPJavaCurriculumDesign\settings.gradle`](../../settings.gradle) 中：

```kotlin
include ':backend'
include ':frontend-android'
```

使用 `include` 导入子项目。

> 返回主页：[OOPJavaCurriculumDesign](../index.md)
>
> 下一篇：[Backend: 构建 Netty HTTP API 服务，封装](02-build-netty-http-api.md)