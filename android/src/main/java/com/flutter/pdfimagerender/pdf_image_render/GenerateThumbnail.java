package com.flutter.pdfimagerender.pdf_image_render;

import android.graphics.Bitmap;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.tom_roush.pdfbox.pdmodel.PDDocument;
import com.tom_roush.pdfbox.rendering.PDFRenderer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import io.flutter.Log;
import io.flutter.plugin.common.EventChannel;

public class GenerateThumbnail implements Runnable {
    final private String path;
    final private EventChannel.EventSink events;

    GenerateThumbnail(String path, EventChannel.EventSink events) {
        this.events = events;
        this.path = path;
    }

    public void main() {
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void run() {
        generateThumbnails(path);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void generateThumbnails(String path) {
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
                    events.success(pageImage);
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
}
