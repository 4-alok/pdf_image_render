import 'dart:io';
import 'dart:ui';
import 'package:flutter/material.dart';
import 'package:pdf_image_render/pdf_image_render.dart';

void main() {
  runApp(MyApp());
}

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  static final String filePath = "/storage/emulated/0/ADM/TEMP/TaGR.pdf";

  void getPageCount(String path) async {
    final int? page = await PdfImageRender.pageCount(path);
    print(page);
  }

  void delete(String path) async {
    Directory dir = Directory(path);
    for (FileSystemEntity e in dir.listSync()) {
      if ((e is File) && (e.path != filePath)) {
        e.deleteSync();
      }
    }
  }

  void test() async {
    await PdfImageRender.test();
  }

  @override
  void initState() {
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      theme: ThemeData(brightness: Brightness.dark),
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: Container(
          width: window.physicalSize.width,
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            crossAxisAlignment: CrossAxisAlignment.center,
            mainAxisSize: MainAxisSize.max,
            children: [
              button(
                onTap: () => getPageCount(filePath),
                text: "Get Page count",
              ),
              button(
                onTap: () {
                  PdfImageRender.thumbnailStream(filePath)?.listen((event) {
                    print(event);
                  });
                },
                text: "Generate image",
              ),
              button(
                onTap: () => test(),
                text: "Test",
              ),
              button(
                onTap: () => delete(Directory(filePath).parent.path),
                text: "Delete",
              ),
              Container(
                margin: EdgeInsets.only(top: 50),
                width: window.physicalSize.width,
                child: Column(
                  children: [
                    rowAnim(),
                    rowAnim(),
                  ],
                ),
              )
            ],
          ),
        ),
      ),
    );
  }

  Row rowAnim() {
    return Row(
      mainAxisSize: MainAxisSize.max,
      mainAxisAlignment: MainAxisAlignment.spaceEvenly,
      children: [
        animat(),
        animat(),
        animat(),
        animat(),
        animat(),
        animat(),
        animat(),
        animat(),
        animat(),
        animat(),
      ],
    );
  }

  Widget button({required Function onTap, required String text}) {
    return Container(
      padding: const EdgeInsets.only(top: 20),
      child: ElevatedButton(
        onPressed: () => onTap(),
        child: Text(text),
      ),
    );
  }

  Widget animat() => CircularProgressIndicator();
}
