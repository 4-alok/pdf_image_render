package com.flutter.pdfimagerender.pdf_image_render;

import android.graphics.Bitmap;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.tom_roush.pdfbox.pdmodel.PDDocument;
import com.tom_roush.pdfbox.rendering.PDFRenderer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import io.flutter.Log;
import io.flutter.plugin.common.EventChannel;

public class EventChannelHandler implements EventChannel.StreamHandler {


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onListen(Object arguments, EventChannel.EventSink events) {
        @SuppressWarnings("unchecked") List<String> args = (List<String>) arguments;
        final String path = args.get(0);
        generateThumbnails(path, events);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void generateThumbnails(String path, EventChannel.EventSink events) {
        PDDocument document = null;
        try {
            File renderFile = new File(path);
            document = PDDocument.load(renderFile);
            PDFRenderer pdfRenderer = new PDFRenderer(document);

            for (int page = 0; page < document.getNumberOfPages(); ++page) {
                final Bitmap pageImage = pdfRenderer.renderImageWithDPI(page, 30, Bitmap.Config.RGB_565);
                final String imgPath = path.substring(0, path.length() - 4) + "-" + page + ".png";
                try (FileOutputStream out = new FileOutputStream(imgPath)) {
                    pageImage.compress(Bitmap.CompressFormat.PNG, 100, out);
                    events.success(imgPath);
                } catch (IOException e) {
                    events.success("0");
                    Log.e("Pdf Image Render :", "Exception thrown while saving bitmap", e);
                }
            }
        } catch (IOException e) {
            Log.e("Pdf Image Render :", "Exception thrown while loading document to strip", e);
        } finally {
            try {
                if (document != null) document.close();
            } catch (IOException e) {
                Log.e("Pdf Image Render :", "Exception thrown while closing document", e);
            }
        }
    }

    @Override
    public void onCancel(Object arguments) {}
}
