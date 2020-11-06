import 'dart:async';

import 'package:flutter/services.dart';

class FlutterPluginLame {
  static const MethodChannel _channel = const MethodChannel('flutter_plugin_lame');


  static Future<String> get startRecord async {
    final String startRecord = await _channel.invokeMethod('startRecord');
    return startRecord;
  }

  static Future<String> get stopRecord async {
    final String stopRecord = await _channel.invokeMethod('stopRecord');
    return stopRecord;
  }

  static Future<String> get playRecord async {
    final String playRecord = await _channel.invokeMethod('playRecord');
    return playRecord;
  }
}
