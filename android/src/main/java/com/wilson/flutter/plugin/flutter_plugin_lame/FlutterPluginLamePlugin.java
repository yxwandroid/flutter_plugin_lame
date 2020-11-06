package com.wilson.flutter.plugin.flutter_plugin_lame;

import androidx.annotation.NonNull;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;
public class FlutterPluginLamePlugin implements FlutterPlugin, MethodCallHandler {
  private MethodChannel channel;
  private Mp3AudioRecordManager mp3AudioRecordManager;

  @Override
  public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
    channel = new MethodChannel(flutterPluginBinding.getBinaryMessenger(), "flutter_plugin_lame");
    channel.setMethodCallHandler(this);
    mp3AudioRecordManager = Mp3AudioRecordManager.getInstance();
  }

  @Override
  public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
    if (call.method.equals("startRecord")) {
      boolean isRecording = mp3AudioRecordManager.isRecording;
      if (!isRecording) {
        new Thread() {
          @Override
          public void run() {
            mp3AudioRecordManager.isRecording = true;
            mp3AudioRecordManager.startRecording();
          }
        }.start();

      }
      result.success("startRecord");
    } else if (call.method.equals("stopRecord")) {
      mp3AudioRecordManager.isRecording = false;
      result.success("stopRecord");
    } else if (call.method.equals("playRecord")){
//            String path = call.arguments;
      PlayUtilsPlus play =new PlayUtilsPlus();
      String filePath = mp3AudioRecordManager.filePath;
      play.startPlaying(filePath);
      result.success(filePath);
    }else {

    }
  }

  @Override
  public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
    channel.setMethodCallHandler(null);
  }
}
