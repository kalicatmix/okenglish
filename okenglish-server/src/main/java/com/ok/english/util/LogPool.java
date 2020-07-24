package com.ok.english.util;

import java.util.ArrayList;

public class LogPool {
    private static ArrayList<String> logs = new ArrayList<>();

    public static void addLog(String log) {
        logs.add(log);
    }

    public static void clearPool() {
        logs.clear();
    }

    public static ArrayList<String> getAllLogs() {
        return logs;
    }
}
