package com.flutter.pdfimagerender.pdf_image_render;

import android.graphics.Bitmap;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.tom_roush.pdfbox.pdmodel.PDDocument;
import com.tom_roush.pdfbox.util.PDFBoxResourceLoader;

import java.io.File;
import java.io.IOException;

import io.flutter.Log;
import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.EventChannel;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;

public class PdfImageRenderPlugin implements FlutterPlugin, MethodCallHandler {
    final private String streamID = "pdf_image_render_stream";
    final private String channelID = "pdf_image_render";
    private MethodChannel channel;
    private EventChannel eventChannel;
    @Override

    public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
        PDFBoxResourceLoader.init(flutterPluginBinding.getApplicationContext());
        channel = new MethodChannel(flutterPluginBinding.getBinaryMessenger(), channelID);
        eventChannel = new EventChannel(flutterPluginBinding.getBinaryMessenger(), streamID);
        channel.setMethodCallHandler(this);
        EventChannelHandler eventChannelHandler = new EventChannelHandler();
        eventChannel.setStreamHandler(eventChannelHandler);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
        switch (call.method) {
            case "getPlatformVersion":
                result.success("Android " + android.os.Build.VERSION.RELEASE);
                break;
            case "getPageCount": {
                final String path = call.argument("path");
                int pageCount = getPageCount(path);
                result.success(pageCount);
                break;
            }

            default:
                result.notImplemented();
                break;
        }
    }

    private int getPageCount(String path) {
        PDDocument document = null;
        try {
            File renderFile = new File(path);
            document = PDDocument.load(renderFile);
        } catch (IOException e) {
            Log.e("Pdf Image Render :", "Exception thrown while loading document to strip", e);
        } finally {
            try {
                if (document != null) document.close();
            } catch (IOException e) {
                Log.e("Pdf Image Render :", "Exception thrown while closing document", e);
            }
        }
        return document != null ? document.getNumberOfPages() : 0;
    }

    private Bitmap decreaseBitmapSize(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();
        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    @Override
    public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
        channel.setMethodCallHandler(null);
    }
}