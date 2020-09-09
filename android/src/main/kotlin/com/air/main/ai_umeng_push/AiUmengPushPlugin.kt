package com.air.main.ai_umeng_push

import android.app.Notification
import android.content.Context
import android.os.Handler
import android.os.Message
import android.util.Log
import android.widget.RemoteViews
import androidx.annotation.NonNull
import com.umeng.commonsdk.UMConfigure
import com.umeng.message.IUmengRegisterCallback
import com.umeng.message.PushAgent
import com.umeng.message.UHandler
import com.umeng.message.UmengMessageHandler
import com.umeng.message.entity.UMessage
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import io.flutter.plugin.common.PluginRegistry.Registrar


/** AiUmengPushPlugin */
public class AiUmengPushPlugin : FlutterPlugin, MethodCallHandler {
    /// The MethodChannel that will the communication between Flutter and native Android
    ///
    /// This local reference serves to register the plugin with the Flutter Engine and unregister it
    /// when the Flutter Engine is detached from the Activity
    private lateinit var channel: MethodChannel
    private var mHandler: Handler = Handler(
            Handler.Callback { msg ->
                when (msg?.what) {
                    //成功
                    200 -> {
                        val deviceToken = msg.data.getString("deviceToken");
                        //注册成功会返回deviceToken
                        channel.invokeMethod("registerSuccess", mutableMapOf(
                                Pair("success", true),//注册成功
                                Pair("deviceToken", deviceToken)//deviceToken
                        ));
                        //该方法是【友盟+】Push后台进行日活统计及多维度推送的必调用方法，请务必调用！
                        PushAgent.getInstance(mContext).onAppStart();
                    }
                    500 -> {
                        val deviceToken = msg.data.getString("deviceToken");
                        val errorInfo1 = msg.data.getString("errorInfo1");
                        val errorInfo2 = msg.data.getString("errorInfo2");

                        channel.invokeMethod("registerFailure", mutableMapOf(
                                Pair("success", false),//注册失败
                                Pair("deviceToken", ""),//deviceToken
                                Pair("errorInfo1", errorInfo1),//deviceToken
                                Pair("errorInfo2", errorInfo2)//deviceToken
                        ));
                    }

                }
                true;
            }
    );


    override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
        channel = MethodChannel(flutterPluginBinding.getFlutterEngine().getDartExecutor(), "ai_umeng_push")
        channel.setMethodCallHandler(this);
        //set application context value
        mContext = flutterPluginBinding.applicationContext;

    }

    // This static function is optional and equivalent to onAttachedToEngine. It supports the old
    // pre-Flutter-1.12 Android projects. You are encouraged to continue supporting
    // plugin registration via this function while apps migrate to use the new Android APIs
    // post-flutter-1.12 via https://flutter.dev/go/android-project-migration.
    //
    // It is encouraged to share logic between onAttachedToEngine and registerWith to keep
    // them functionally equivalent. Only one of onAttachedToEngine or registerWith will be called
    // depending on the user's project. onAttachedToEngine or registerWith must both be defined
    // in the same class.
    companion object {
        @JvmStatic
        fun registerWith(registrar: Registrar) {
            val channel = MethodChannel(registrar.messenger(), "ai_umeng_push")
            channel.setMethodCallHandler(AiUmengPushPlugin())
            //set application context value
            mContext = registrar.context().applicationContext;
        }

        //application context
        private lateinit var mContext: Context
    }

    override fun onMethodCall(@NonNull call: MethodCall, @NonNull result: Result) {
        if (call.method == "getPlatformVersion") {
            result.success("Android ${android.os.Build.VERSION.RELEASE}")
        } else if (call.method == "init") {
            val appkey: String = call.argument("appKey")!!;
            val pushSecret: String = call.argument("pushSecret")!!;
            val channel: String = call.argument("channel")!!;
            UMConfigure.init(mContext, appkey, channel, UMConfigure.DEVICE_TYPE_PHONE, pushSecret);
            registerUMengPushService();
//      UMConfigure.init(Context context, String appkey, String channel, int deviceType, String pushSecret);
        } else {
            result.notImplemented()
        }
    }

    override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
        channel.setMethodCallHandler(null)

    }

    /**
     * Register UMeng push service
     */
    private fun registerUMengPushService() {

        val pushAgent = PushAgent.getInstance(mContext);

        pushAgent.register(
                object : IUmengRegisterCallback {
                    override fun onSuccess(deviceToken: String) {


                        val message = Message();
                        message.what = 200;
                        message.data.putString("deviceToken", deviceToken);
                        mHandler.sendMessage(message);

                    }

                    override fun onFailure(s: String, s1: String) {
                        val message = Message();
                        message.what = 500;
                        val data = message.data;
                        data.putString("deviceToken", "");
                        data.putString("errorInfo1", s);
                        data.putString("errorInfo2", s1);
                        mHandler.sendMessage(message);

                    }
                }
        );
        //消息到达时处理
        //友盟推送，默认情况下会在收到消息后显示通知！
        //如果启动下面这行操作，则会导致接收消息后不显示通知，如需显示通知还需主动的代码配置显示通知！
        pushStyle2(pushAgent);
    }
    private fun pushStyle1(pushAgent:PushAgent){
        pushAgent.setMessageHandler(UmengMessageHandler());
    }

    private fun pushStyle2(pushAgent:PushAgent){
        pushAgent.messageHandler = object : UmengMessageHandler() {
            override fun handleMessage(context: Context?, msg: UMessage?) {

                //通知
                Log.e("AiUmengPushPlugin","收到消息：${msg.toString()}");
//                val pushPublish = PushPublish()
//                pushPublish.setId(msg?.msg_id)
//                pushPublish.setMsgTip(msg?.text)
//                pushPublish.setTitle(msg?.title)


                //更改在主线程操作
//                val mSubscription: Subscription = Observable.just(true)
//                        .subscribeOn(Schedulers.io())
//                        .observeOn(AndroidSchedulers.mainThread())
//                        .subscribe(object : Subscriber<Boolean?>() {
//                            fun onCompleted() {
//                                /*
//                            以“观察者思想”的姿态，通知注册接收信息的监听者(也就是：添加监听回调的位置)
//                             */
//                                notifyRegisterReceiver(pushPublish)
//                            }
//
//                            fun onError(e: Throwable?) {}
//                            fun onNext(aBoolean: Boolean?) {}
//                        })

                /*
            下面代码是按照官方文档编写的，但是在调试中发现一个问题：导致通知信息不显示，但是也没有异常日志出现
            调试
            调试
            再调试
            进一步调试
            经过艰难的调试
            最后找到了原因
            原因是：下面的代码出现了空指针！
            哦，出现了空指针
            为什么官方代码这么***,
            省略若干文字。。。
             */
                if (msg != null) {
                    if (msg.extra != null) {
                        if (msg.extra.entries != null) {
                            for ((key1, value1) in msg.extra.entries) {
                                val key = key1!!
                                val value = value1!!
                                Log.e("PushManager", "key:$key,value:$value")
                            }
                        }
                    }
                }

                //通知
//                when (msg?.builder_id) {
//                     1 ->{
//
//                         val builder = Notification.Builder(context)
//                         val myNotificationView = RemoteViews(context!!.packageName,
//                                 R.layout.notification_view)
//                         myNotificationView.setTextViewText(R.id.notification_title, msg!!.title)
//                         myNotificationView.setTextViewText(R.id.notification_text, msg.text)
//                         myNotificationView.setImageViewBitmap(R.id.notification_large_icon,
//                                 getLargeIcon(context, msg))
//                         myNotificationView.setImageViewResource(R.id.notification_small_icon,
//                                 getSmallIconId(context, msg))
//                         builder.setContent(myNotificationView)
//                                 .setSmallIcon(getSmallIconId(context, msg))
//                                 .setTicker(msg.ticker)
//                                 .setAutoCancel(true)
//                         return builder.notification
//                     }
//
//                    else->
//                    //默认为0，若填写的builder_id并不存在，也使用默认。
//                    return super.getNotification(context, msg);
//                }

            }

        };
    }

}
