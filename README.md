# ai_umeng_push

A new Flutter plugin for umeng push.


![totem](https://raw.githubusercontent.com/pdliuw/pdliuw.github.io/master/images/totem_four_logo.jpg)

-----


## 1.安装

使用当前包作为依赖库

### 1. 依赖此库

在文件 'pubspec.yaml' 中添加

[![pub package](https://img.shields.io/pub/v/ai_umeng_push.svg)](https://pub.dev/packages/ai_umeng_push)

```

dependencies:

  ai_umeng_push: ^version

```

或者以下方式依赖

```
dependencies:

  # ai_umeng_push package.
  ai_umeng_push:
    git:
      url: https://github.com/pdliuw/ai_umeng_push.git

```

### 2. 安装此库

你可以通过下面的命令行来安装此库

```

$ flutter pub get


```

你也可以通过项目开发工具通过可视化操作来执行上述步骤

### 3. 导入此库

现在，在你的Dart编辑代码中，你可以使用：

```

import 'package:ai_umeng_push/ai_umeng_push.dart';

```


## 2.使用

使用'友盟-推送服务'需要动态申请权限，动态权限推荐：[permission_handler](https://github.com/Baseflow/flutter-permission-handler)


Platform配置及使用

<details>
<summary>Android</summary>

* project -> build.gradle

```

        buildscript {
            repositories {
                google()
                jcenter()
                maven { url 'https://dl.bintray.com/umsdk/release' } // add+
            }
            ***
        }
        
        allprojects {
            repositories {
                maven {
                    url "https://maven.google.com"
                }
                google()
                jcenter()
                maven { url "https://jitpack.io" }
                mavenCentral() // add+
                maven { url 'https://dl.bintray.com/umsdk/release' } // add+
            }
        }

```

```

        <!--
        友盟推送服务权限
        -->
        <uses-permission android:name="android.permission.INTERNET"/>
        <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
        <uses-permission android:name="android.permission.ACCESS_WIFI_STATE"/>
        <uses-permission android:name="android.permission.READ_PHONE_STATE"/>
        <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION"/>
        <uses-permission android:name="android.permission.CHANGE_WIFI_MULTICAST_STATE"/>
        <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
        <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"/>   
    

    


```

* proguard

```

        #（友盟推送--start）
        -dontwarn com.umeng.**
        -dontwarn com.taobao.**
        -dontwarn anet.channel.**
        -dontwarn anetwork.channel.**
        -dontwarn org.android.**
        -dontwarn org.apache.thrift.**
        -dontwarn com.xiaomi.**
        -dontwarn com.huawei.**
        -dontwarn com.meizu.**
        
        -keepattributes *Annotation*
        
        -keep class com.taobao.** {*;}
        -keep class org.android.** {*;}
        -keep class anet.channel.** {*;}
        -keep class com.umeng.** {*;}
        -keep class com.xiaomi.** {*;}
        -keep class com.huawei.** {*;}
        -keep class com.meizu.** {*;}
        -keep class org.apache.thrift.** {*;}
        
        -keep class com.alibaba.sdk.android.**{*;}
        -keep class com.ut.**{*;}
        -keep class com.ta.**{*;}
        
        -keep public class **.R$*{
           public static final int *;
        }
        -keepclassmembers class * {
           public <init>(org.json.JSONObject);
        }
        #（友盟推送--end）

```

* 监听'初始化'结果

```

                      //接收初始化结果
                      AiUmengPush.registerServiceCallback(
                        success: (deviceToken) {
                          //deviceToken:设备标识
                        },
                        failure: (errorInfo) {
                          //errorInfo:初始化失败信息
                        },
                      );


```

* 初始化

```

                      //初始化
                      AiUmengPush.init(
                          appKey: "${appKey}",
                          pushSecret: "${secret}",
                          channel: "${channelName}");

```



</details>