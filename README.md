# ai_umeng_push

A new Flutter plugin for umeng push.


![totem](https://raw.githubusercontent.com/pdliuw/pdliuw.github.io/master/images/totem_four_logo.jpg)

-----


## 2.使用

使用'友盟-推送服务'需要动态申请权限，动态权限推荐：[permission_handler](https://github.com/Baseflow/flutter-permission-handler)

配置权限

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

* init result callback

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

* init

```

                      //初始化
                      AiUmengPush.init(
                          appKey: "${appKey}",
                          pushSecret: "${secret}",
                          channel: "${channelName}");

```



</details>