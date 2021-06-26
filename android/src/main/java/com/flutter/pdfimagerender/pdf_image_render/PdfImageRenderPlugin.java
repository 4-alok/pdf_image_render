package com.flutter.pdfimagerender.pdf_image_render;

import android.content.Context;
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
    private Context context;
    private EventChannel eventChannel;

    @Override

    public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
        PDFBoxResourceLoader.init(flutterPluginBinding.getApplicationContext());
        channel = new MethodChannel(flutterPluginBinding.getBinaryMessenger(), channelID);
        eventChannel = new EventChannel(flutterPluginBinding.getBinaryMessenger(), streamID);
        channel.setMethodCallHandler(this);
        eventChannel.setStreamHandler(new EventChannelHandler());
        context = flutterPluginBinding.getApplicationContext();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
        switch (call.method) {
            case "getPlatformVersion":
                result.success("Android " + android.os.Build.VERSION.RELEASE);
                break;
            case "getPageCount": {
                new GetPageCount(result, call.argument("path"));
                break;
            }
            case "test": {
                result.success("Test");
                break;
            }
            default:
                result.notImplemented();
                break;
        }
    }

    @Override
    public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
        channel.setMethodCallHandler(null);
        eventChannel.setStreamHandler(null);
    }
}
