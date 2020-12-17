import 'package:flutter/material.dart';
import 'dart:async';

import 'package:flutter_plugin_lame/flutter_plugin_lame.dart';

void main() {
  runApp(MyApp());
}

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  String _platformVersion = 'Unknown';

  @override
  void initState() {
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: Column(
          children: [
            FlatButton(
                onPressed: () async {
                  await FlutterPluginLame.startRecord;

                  // Timer(Duration(seconds: 4), () async {
                  //   await FlutterPluginLame.stopRecord;
                  //   print("flutter 停止录制");
                  // });
                },
                child: Text("开始录制")),
            FlatButton(
                onPressed: () async {
                  await FlutterPluginLame.stopRecord;
                },
                child: Text("结束录制")),
            FlatButton(
                onPressed: () async {
                  String filePath= await FlutterPluginLame.playRecord;

                },
                child: Text("播放")),
          ],
        ),
      ),
    );
  }
}
