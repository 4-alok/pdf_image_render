package com.flutter.pdfimagerender.pdf_image_render;
import android.os.Handler;
import android.os.Looper;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.SettableFuture;
import com.tom_roush.pdfbox.pdmodel.PDDocument;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import io.flutter.Log;
import io.flutter.plugin.common.MethodChannel.Result;

public class GetPageCount {
    GetPageCount(Result result, String path){
        executeInBackground(() ->runInBackground(path), result);
    }

    private final Executor uiThreadExecutor = new UiThreadExecutor();
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    private int runInBackground(String path) {
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

    private <T> void executeInBackground(Callable<T> task, Result result) {
        final SettableFuture<T> future = SettableFuture.create();
        Futures.addCallback(
                future,
                new FutureCallback<T>() {
                    public void onSuccess(T answer) {
                        result.success(answer);
                    }

                    public void onFailure(Throwable t) {
                        result.error(t.getClass().getName(), t.getMessage(), null);
                    }
                },
                uiThreadExecutor);
        executor.execute(
                () -> {
                    try {
                        future.set(task.call());
                    } catch (Throwable t) {
                        future.setException(t);
                    }
                });
    }

    private static class UiThreadExecutor implements Executor {
        private final Handler handler = new Handler(Looper.getMainLooper());
        @Override
        public void execute(Runnable command) {
            handler.post(command);
        }
    }
}