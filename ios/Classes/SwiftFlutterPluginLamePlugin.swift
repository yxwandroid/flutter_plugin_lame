import Flutter
import UIKit
import LameTool

public class SwiftFlutterPluginLamePlugin: NSObject, FlutterPlugin {
  public static func register(with registrar: FlutterPluginRegistrar) {
    let channel = FlutterMethodChannel(name: "flutter_plugin_lame", binaryMessenger: registrar.messenger())
    let instance = SwiftFlutterPluginLamePlugin()
    registrar.addMethodCallDelegate(instance, channel: channel)
  }
  let recoder_manager = RecordManager()//初始化
  public func handle(_ call: FlutterMethodCall, result: @escaping FlutterResult) {
     if call.method=="startRecord"{
        startRecord()
        result("startRecord")
    }else if call.method=="stopRecord"{
        stopRecord()
        result("stopRecord")
    }else if call.method=="playRecord"{
        playRecord()
        let filePath = ""
        result(filePath)
    }
   
  }

    
     func startRecord() {
        recoder_manager.beginRecord()//开始录音
    }
    
     func stopRecord() {
        recoder_manager.stopRecord()//结束录音
    }
     func playRecord() {
        recoder_manager.play()//播放录制的音频
    }

     func record(_ sender: UIButton) {
        let input = Bundle.main.path(forResource: "file_example_WAV_10MG", ofType: "wav")!
        let output = FileManager.default.temporaryFileURL(fileName: "\(UUID().uuidString).mp3")!

        AudioConverter.encodeToMp3(
            inPcmPath: input,
            outMp3Path: output.path,
            onProgress: { [unowned self] progress in
                print( "Progress: \(Int(100 * progress))")
            }, onComplete: {
                print(output.path)
                print( "Complete")
        })
    }
}



extension FileManager {

    func temporaryFileURL(fileName: String = UUID().uuidString) -> URL? {
        return URL(fileURLWithPath: NSTemporaryDirectory(), isDirectory: true).appendingPathComponent(fileName)
    }
}

infix operator -->
// prepare class instance
func --> <T>(object: T, closure: (T) -> Void) -> T {
    closure(object)
    return object
}

