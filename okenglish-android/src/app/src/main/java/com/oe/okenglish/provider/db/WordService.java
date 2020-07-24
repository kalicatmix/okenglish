package com.oe.okenglish.provider.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.oe.okenglish.entity.Word;
import com.oe.okenglish.kit.AppKit;
import com.oe.okenglish.res.Res;

import java.nio.charset.Charset;
import java.util.ArrayList;

public class WordService {
    private SQLiteDatabase sqLiteDatabase;
    private static WordService dataBaseService;

    private WordService(Context context) {
        String dbPath = context.getExternalFilesDir(null).getAbsolutePath() + "/" + Res.OKENGLISH_DB;
        sqLiteDatabase = SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.OPEN_READONLY);
    }

    public static WordService getInstance(Context context) {
        synchronized (WordService.class) {
            if (dataBaseService == null) dataBaseService = new WordService(context);
        }
        return dataBaseService;
    }

    public Word queryWordById(int id) {
        String wordName = Res.NONE_STRING;
        String translate = Res.NONE_STRING;
        byte[] voice = new byte[]{};
        try {
            Cursor wordCursor = sqLiteDatabase.rawQuery(Res.SQL_QUERY_WORD, new String[]{AppKit.toString(id)});
            if (wordCursor.moveToNext()) {
                wordName = wordCursor.getString(0);
                byte[] wordBuffer = wordCursor.getBlob(1);
                byte[] wordTemp = new byte[wordBuffer.length - 1];
                for (int i = 0; i < wordBuffer.length - 1; i++) wordTemp[i] = wordBuffer[i];
                translate = new String(wordTemp, Charset.forName("utf8"));
            }
            wordCursor.close();
            Cursor voiceCursor = sqLiteDatabase.rawQuery(Res.SQL_QUERY_VOICE, new String[]{wordName});
            if (voiceCursor.moveToNext()) {
                voice = voiceCursor.getBlob(0);
            }
            voiceCursor.close();
        } catch (Exception e) {
            AppKit.log("queryWorldById", e.getMessage());
        }
        Word word = new Word(wordName, translate, voice);
        return word;
    }

    public ArrayList<Integer> queryAllWordByPlanId(int id) {
        ArrayList<Integer> wordId = new ArrayList<Integer>();
        Cursor cursor = sqLiteDatabase.rawQuery(Res.SQL_QUERY_ID_BY_PLAN, new String[]{AppKit.toString(id)});
        while (cursor.moveToNext()) {
            wordId.add(cursor.getInt(0));
        }
        return wordId;
    }
}
