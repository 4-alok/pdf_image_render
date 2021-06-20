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

  void generateImage(String path) async {
    final bool done = await PdfImageRender.generateThumbnails(path);
    print(done);
  }

  @override
  void initState() {
    PdfImageRender().thumbnailStream(filePath)?.listen((event) {
      print(event);
    });

    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: Container(
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
                onTap: () => generateImage(filePath),
                text: "Generate image",
              )
            ],
          ),
        ),
      ),
    );
  }

  Padding button({required Function onTap, required String text}) {
    return Padding(
      padding: const EdgeInsets.all(8.0),
      child: ElevatedButton(
        onPressed: () => onTap(),
        child: Text(text),
      ),
    );
  }
}
