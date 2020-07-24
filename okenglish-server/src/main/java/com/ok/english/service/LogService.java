package com.ok.english.service;

import com.ok.english.util.LogPool;

import java.util.Date;

public class LogService {

    public static void produceLog(String log) {
        LogPool.addLog(new Date() + ":" + log);
    }
}
