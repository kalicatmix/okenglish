package com.oe.okenglish.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.oe.okenglish.kit.AppKit;
import com.oe.okenglish.provider.db.UserService;
import com.oe.okenglish.res.Res;

public class OkEnglishProvider extends ContentProvider {
    public static final int USER_OP = 0xffff;
    public static final int FAVO_OP = 0xfffe;
    public static final String USER_PATH = "user";
    public static final String FAVO_PATH = "favo";
    private UserService userService;
    UriMatcher uriMatcher;

    @Override
    public boolean onCreate() {
        userService = new UserService(getContext().getApplicationContext(), Res.USER_DB, null, Res.USER_DB_VERSION);;
        uriMatcher = new UriMatcher(0x0001);
        uriMatcher.addURI(Res.AUTHORITIES, USER_PATH, USER_OP);
        uriMatcher.addURI(Res.AUTHORITIES, FAVO_PATH, FAVO_OP);
        AppKit.log("okEnglishProvider", "onCreate");
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor cursor = null;
        switch (uriMatcher.match(uri)) {
            case USER_OP:
                cursor = userService.getReadableDatabase().query("user", projection, selection, selectionArgs, null, null, null);
                break;
            case FAVO_OP:
                cursor = userService.getReadableDatabase().query("favo", projection, selection, selectionArgs, null, null, null);
                break;
        }
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return "text/plan";
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        switch (uriMatcher.match(uri)) {
            case USER_OP:
                int uId = values.getAsInteger("id");
                String name = values.getAsString("name");
                int plan = values.getAsInteger("plan");
                int dayWord = values.getAsInteger("dayWord");
                userService.getWritableDatabase().execSQL(Res.SQL_INSERT_USERTB, new Object[]{uId, name, plan, dayWord});
                break;
            case FAVO_OP:
                int fId = values.getAsInteger("id");
                String word = values.getAsString("word");
                String trans = values.getAsString("trans");
                userService.getWritableDatabase().execSQL(Res.SQL_INSERT_FAVORITETB, new Object[]{fId, word, trans});
                break;

        }
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        switch (uriMatcher.match(uri)) {
            case USER_OP:
                userService.getWritableDatabase().delete("user", selection, selectionArgs);
                break;
            case FAVO_OP:
                userService.getWritableDatabase().delete("favo", selection, selectionArgs);
                break;


        }
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        switch (uriMatcher.match(uri)) {
            case USER_OP:
                userService.getWritableDatabase().update("user", values, selection, selectionArgs);
                break;
            case FAVO_OP:
                userService.getWritableDatabase().update("favo", values, selection, selectionArgs);
                break;
        }
        return 0;
    }
}
