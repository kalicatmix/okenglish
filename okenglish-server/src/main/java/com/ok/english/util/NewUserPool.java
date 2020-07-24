package com.ok.english.util;

import com.ok.english.entity.User;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

public class NewUserPool {
    private static ConcurrentHashMap<String, User> pool = new ConcurrentHashMap<>();

    public static User getUser(String name) {
        return pool.get(name);
    }

    public static void addUser(String name, User user) {
        pool.put(name, user);
    }

    public static int getPoolSize() {
        return pool.size();
    }

    public static void clearPool() {
        pool.clear();
    }

    public static Collection<User> getAllUser() {
        return pool.values();
    }
}
