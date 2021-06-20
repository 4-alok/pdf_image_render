package com.flutter.pdfimagerender.pdf_image_render;

import java.util.List;

import io.flutter.Log;
import io.flutter.plugin.common.EventChannel;

public class EventChannelHandler implements EventChannel.StreamHandler {

    @Override
    public void onListen(Object arguments, EventChannel.EventSink events) {
        List<String> args = (List<String>) arguments;
        final String path =(String) args.get(0);
        GenerateThumbnail gt = new GenerateThumbnail(path, events);
        Thread thread = new Thread(gt);
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            Log.e("Pdf Image Render :", "Exception thrown while thread join", e);
            e.printStackTrace();
        }
        Log.e("Pdf Image Render :", "-----------> Work done");
    }

    @Override
    public void onCancel(Object arguments) {

    }
}
