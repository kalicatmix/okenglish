package com.oe.okenglish.kit;

import android.widget.Toast;

import com.oe.okenglish.application.OKApplication;

import java.io.File;

public class OkCheckTool {
    public static boolean isFileExist(String path){
        return  new File(path).exists();
    }
    public static boolean isNetworkConnected(OKApplication okApplication){
        if(okApplication.isNetwork()){
            return true;
        }
        Toast.makeText(okApplication,"请检查网络",Toast.LENGTH_SHORT).show();
        return false;
    }
}
