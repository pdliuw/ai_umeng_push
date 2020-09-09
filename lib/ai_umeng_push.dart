import 'dart:async';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

class AiUmengPush {
  static const MethodChannel _channel = const MethodChannel('ai_umeng_push');

  static Future<String> get platformVersion async {
    final String version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }

  static init({
    @required String appKey,
    @required String pushSecret,
    @required String channel,
  }) async {
    print("init");
    _channel.invokeMethod("init", {
      "appKey": appKey,
      "pushSecret": pushSecret,
      "channel": channel,
    });
  }

  ///
  /// registerServiceCallback
  static registerServiceCallback({
    @required ValueChanged<String> success,
    @required ValueChanged<String> failure,
  }) async {
    print("registerServiceCallback");
    _channel.setMethodCallHandler((MethodCall call) {
      String method = call.method;
      print("method:${method},arguments:${call.arguments}");
      switch (method) {
        case "registerSuccess":
          String deviceToken = call.arguments['deviceToken'];
          success(deviceToken);
          break;

        case "registerFailure":
          String errorInfo1 = call.arguments['errorInfo1'];
          String errorInfo2 = call.arguments['errorInfo2'];
          failure("$errorInfo1,$errorInfo2");
          break;

        default:
      }
      return null;
    });
  }
}
