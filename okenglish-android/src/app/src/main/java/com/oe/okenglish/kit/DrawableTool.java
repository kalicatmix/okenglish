package com.oe.okenglish.kit;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

public class DrawableTool {
    public static Drawable zoomDrawable(Context context, int id, int width, int height) {
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), id);
        return new BitmapDrawable(context.getResources(),bitmap.createScaledBitmap(bitmap, width, height, false));
        }
        }
