package com.ok.english.service;

import com.ok.english.entity.Admin;
import com.ok.english.entity.Message;
import com.ok.english.entity.Notification;

public interface AdminDao {
    boolean adminLogin(Admin admin);

    Message getAllOnlineUsers();

    Message getAllNewUsers();

    Message getAllLogs();

    Message getNotification(int limit);

    boolean addNotification(Notification notification);

    Message getAllUsers();

    Message getApkVersion();
    Message updateApkVersion(String version);
}
