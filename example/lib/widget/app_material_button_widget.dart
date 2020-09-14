import 'package:flutter/material.dart';

///
/// AppMaterialButtonWidget
class AppMaterialButtonWidget extends StatefulWidget {
  String text;
  VoidCallback onTap;
  AppMaterialButtonWidget.defaultStyle({
    @required this.text,
    @required this.onTap,
  });
  @override
  _AppMaterialButtonWidgetState createState() =>
      _AppMaterialButtonWidgetState();
}

class _AppMaterialButtonWidgetState extends State<AppMaterialButtonWidget> {
  @override
  Widget build(BuildContext context) {
    return MaterialButton(
      color: Theme.of(context).primaryColor,
      textColor: Colors.white,
      onPressed: widget.onTap,
      child: Text("${widget.text}"),
    );
  }
}
