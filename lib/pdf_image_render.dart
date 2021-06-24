import 'dart:async';
import 'dart:io';

import 'package:flutter/services.dart';

class PdfImageRender {
  static const MethodChannel _channel = const MethodChannel('pdf_image_render');
  static const EventChannel _eventChannel =
      const EventChannel("pdf_image_render_stream");
  static Stream<String>? _thumbnailStream;

  static Future<String?> get platformVersion async {
    final String? version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }

  static Future<int> pageCount(String path) async {
    if (File(path).existsSync()) {
      final int count = await _channel
          .invokeMethod('getPageCount', <String, dynamic>{'path': path});
      return count;
    } else {
      print("File do not exist");
      return -1;
    }
  }

  static Future<void> test() async {
    final String? s = await _channel.invokeMethod('test');
    print(s);
  }

  static Stream<String>? thumbnailStream(String path) {
    if (File(path).existsSync()) {
      print("Started at ${DateTime.now()}");
      _thumbnailStream =
          _eventChannel.receiveBroadcastStream([path]).map((event) => event);
      print("Ended at ${DateTime.now()}");
    } else {
      print("File do not exist");
    }
    return _thumbnailStream;
  }
}
