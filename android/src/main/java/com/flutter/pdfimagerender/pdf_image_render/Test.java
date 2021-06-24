package com.flutter.pdfimagerender.pdf_image_render;
import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.SettableFuture;

import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import io.flutter.Log;
//import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.util.PathUtils;

public class Test {
    private Context context;
    private Result result;
    
    Test(Context crtxt, Result result){
        context = crtxt;
        this.result = result;
    }

    private final Executor uiThreadExecutor = new UiThreadExecutor();
    private ExecutorService executor = Executors.newSingleThreadExecutor();

    public void go(){
//        final String tempPath = Environment.getExternalStorageDirectory().getPath();
//        PathUtils.getDataDirectory(context);
//        Log.d("Path------------------------->", tempPath);
//
//        int coreCount = Runtime.getRuntime().availableProcessors();
//        Log.d("CPU", "CPU count :"+coreCount);
//        executor.execute(new CupCompute());

//        return tempPath;

        executeInBackground(this::runInBackground, result);
    }

    private String runInBackground() {
        Log.d("Isolate", "Running in background");
        return "Success";
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

class CupCompute implements Runnable {
    @Override
    public void run() {
        Log.d("Isolated", "This is from isolated world");
    }
}