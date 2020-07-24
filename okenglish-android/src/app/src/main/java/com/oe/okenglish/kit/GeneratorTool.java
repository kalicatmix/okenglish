package com.oe.okenglish.kit;

import android.content.Context;
import android.graphics.Typeface;

import com.oe.okenglish.application.OKApplication;
import com.oe.okenglish.entity.Plan;
import com.oe.okenglish.res.Res;

import java.security.MessageDigest;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;

import okhttp3.Headers;

public class GeneratorTool {
    public static int generateRandomNumber(int from, int to) {
        Random random = new Random();
        int temp = 0;
        while ((temp = from + random.nextInt(to)) > to) ;
        return temp;
    }

    public static Plan[] generatePlan(Context context, String[][] items) {
        Plan[] plans = new Plan[items.length];
        for (int i = 0; i < items.length; i++) {
            Plan plan = new Plan(items[i][0], items[i][1], DrawableTool.zoomDrawable(context, Integer.parseInt(items[i][2]), 200, 250));
            plans[i] = plan;
        }
        return plans;
    }

    public static String generateVideoSign() {
        /*
        分析网站得出
         */
        Date oDate = new Date();
        int nY = oDate.getYear();
        int nM = oDate.getMonth();
        int nD = oDate.getDate();
        Date newDate = new Date(nY, nM, nD, 0, 0, 0);
        String[] date = "1970-01-01".split("-");
        Date lastDate = new Date(Integer.parseInt(date[0]), (Integer.parseInt(date[1]) - 1), Integer.parseInt(date[2]), 0, 0, 0);
        String result = "";
        newDate.setYear(oDate.getYear());
        lastDate.setYear(70);
        if (newDate.getTime() > lastDate.getTime()) {
            result = AppKit.toString((newDate.getTime() - lastDate.getTime()) / 86400000);
        } else {
            result = date[0] + "年" + date[1] + "月" + date[2] + "日到今天还有（" + Math.abs(newDate.getTime() - lastDate.getTime()) / 86400000 + "）天了";
        }
        try {
            String payload = "iyuba" + result + "voavideo";
            StringBuilder builder = new StringBuilder();
            byte[] buffer = MessageDigest.getInstance("md5").digest(payload.getBytes());
            for (int offset = 0, i = 0; offset < buffer.length; offset++) {
                i = buffer[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    builder.append("0");
                builder.append(Integer.toHexString(i));
            }
            result = builder.toString();
        } catch (Exception e) {
        }
        return result;
    }

    public static String md5Sign(String str) {
        StringBuilder builder = new StringBuilder();
        try {
            byte[] buffer = MessageDigest.getInstance("md5").digest(str.getBytes());
            for (int offset = 0, i = 0; offset < buffer.length; offset++) {
                i = buffer[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    builder.append("0");
                builder.append(Integer.toHexString(i));
            }
        } catch (Exception e) {
        }
        return builder.toString();
    }

    public static Typeface generateTypeFaceFromAsset(Context context, String path) {
        return Typeface.createFromAsset(context.getAssets(), path);
    }

    public static String replaceStr(String raw, String tag, String replace) {
        return raw.replace(tag, replace);
    }

    public static Headers getTokenHeader(OKApplication application) {
        HashMap<String, String> headers = new HashMap<>();
        headers.put(Res.TOKEN_HEADER, application.getSharedPreferences(Res.CURRENT_LOGIN_ACCOUNT, Context.MODE_PRIVATE).getString("token", "token"));
        return Headers.of(headers);
    }
}
