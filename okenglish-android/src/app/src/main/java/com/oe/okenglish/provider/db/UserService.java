package com.oe.okenglish.provider.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.oe.okenglish.kit.ApiKit;
import com.oe.okenglish.kit.AppKit;
import com.oe.okenglish.res.Res;

public class UserService extends SQLiteOpenHelper {
    public UserService(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Res.SQL_CREATE_USERTB);
        db.execSQL(Res.SQL_CREATE_DEFAULT_USER);
        db.execSQL(Res.SQL_CREATE_FAVORITETB);
        db.execSQL(Res.SQL_CREATE_FAVORITETB_DEFALUT);
        AppKit.log("userService", "onCreate");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        AppKit.log("update", "update");
    }

}
