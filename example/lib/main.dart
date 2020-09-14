import 'package:ai_umeng_push_example/widget/app_material_button_widget.dart';
import 'package:airoute/airoute.dart';
import 'package:flutter/material.dart';

import 'package:ai_umeng_push/ai_umeng_push.dart';
import 'package:permission_handler/permission_handler.dart';

void main() {
  runApp(MyApp());
}

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  bool _granted = false;
  @override
  void initState() {
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return Airoute.createMaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: Center(
          child: _granted
              ? AppMaterialButtonWidget.defaultStyle(
                  onTap: () {},
                  text: "测试推送",
                )
              : AppMaterialButtonWidget.defaultStyle(
                  onTap: () {
                    _requestPermission();
                  },
                  text: "请求权限",
                ),
        ),
      ),
    );
  }

  ///
  /// request permission
  _requestPermission() async {
    if (await Permission.phone.request().isGranted) {
      if (await Permission.storage.request().isGranted) {
        setState(() {
          _granted = true;
        });
      }
    }
  }
}
