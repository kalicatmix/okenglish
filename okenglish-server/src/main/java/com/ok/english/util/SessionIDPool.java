package com.ok.english.util;

import java.util.concurrent.ConcurrentHashMap;

public class SessionIDPool {
    private static ConcurrentHashMap<String, String> pool = new ConcurrentHashMap<>();

    public static String getSessionID(String name) {
        return pool.get(name);
    }

    public static void setSessionID(String name, String sessionId) {
        pool.put(name, sessionId);
    }
}
