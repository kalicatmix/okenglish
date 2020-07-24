package com.ok.english.service;

import com.ok.english.entity.Message;
import com.ok.english.entity.User;

public interface UserDao {
    Message userLogin(User user);

    Message changeUserPwd(User user, String pwdHash);

    Message setDayWordPlan(User user, int wordNumber);

    Message getDayWordPlan(User user);

    Message syncWordRecord(User user, String wordJson);

    Message getWordRecord(User user);

    Message getUserStatus(User user);

    Message setUserStatus(User user, int status);

    Message getWorkBookPlan(User user);

    Message setWorkBookPlan(User user, int book);

    Message setWordSize(User user, int size);
}
