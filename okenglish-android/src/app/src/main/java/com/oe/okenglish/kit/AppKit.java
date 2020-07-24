package com.oe.okenglish.kit;

import android.content.Context;
import android.util.Log;

import com.oe.okenglish.callback.OkCallback;
import com.oe.okenglish.res.Res;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class AppKit {
    public static final String OKENGLISH_LOGIN = "com.oe.okenglish.kit.Appkit.OKENGLISH_LOGIN";

    public static void moveAssetFileToExternalStorage(Context context, String path, OkCallback callback) {
        String absolutePath = context.getExternalFilesDir(null) + "/" + path;
        if (OkCheckTool.isFileExist(absolutePath)) {
            callback.call(true);
            AppKit.log("isFileWrote", true);
            return;
        }
        try {
            InputStream inputStream = context.getAssets().open(path);
            FileOutputStream outputStream = new FileOutputStream(absolutePath);
            byte[] buffer = new byte[1024 * 5];
            while (inputStream.read(buffer) != -1) outputStream.write(buffer);
            inputStream.close();
            outputStream.close();
            callback.call(true);
        } catch (Exception e) {
            callback.call(false);
        }
    }

    public static void writeFileToExternalStorage(Context context, byte[] data, String path, String dir, OkCallback callback) {
        String absolutePath = context.getExternalFilesDir(null) + dir + "/" + path;
        String dirPath = context.getExternalFilesDir(null) + dir;
        if (data.length <= 0) {
            AppKit.log("write data length<0", true);
            return;
        }
        ;
        if (!OkCheckTool.isFileExist(dirPath)) {
            new File(dirPath).mkdir();
            AppKit.log("mkdir", dirPath);
        }
        if (OkCheckTool.isFileExist(absolutePath)) {
            callback.call(true);
            AppKit.log("isFileWrote", true);
            return;
        }
        try {
            FileOutputStream outputStream = new FileOutputStream(absolutePath);
            outputStream.write(data);
            outputStream.flush();
            outputStream.close();
            callback.call(true);
        } catch (Exception e) {
            callback.call(false);
            AppKit.log("write error", e.getMessage());
        }
    }

    public static String toString(Object obj) {
        return obj + "";
    }

    public static void log(String name, Object obj) {
        if (Res.ISDEV)
            Log.e(name, toString(obj));
    }
}
