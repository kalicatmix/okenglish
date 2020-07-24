package com.oe.okenglish.application;

import android.app.Application;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.Uri;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.oe.okenglish.entity.User;
import com.oe.okenglish.kit.AppKit;
import com.oe.okenglish.res.Res;
import com.oe.okenglish.view.PlanDetailDialog;

public class OKApplication extends Application {
    private boolean isLogined = false;
    private boolean isNetwork = true;
    private User user;
    private PlanDetailDialog.PlanDelegation planDelegation;

    public PlanDetailDialog.PlanDelegation getPlanDelegation() {
        return planDelegation;
    }

    public void setPlanDelegation(PlanDetailDialog.PlanDelegation planDelegation) {
        this.planDelegation = planDelegation;
    }

    public boolean isNetwork() {
        return isNetwork;
    }

    public void setNetwork(boolean network) {
        isNetwork = network;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isLogined() {
        return isLogined;
    }

    public void setLogined(boolean logined) {
        isLogined = logined;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        user = new User();
        long id = 0;
        if (1 == (id = getSharedPreferences(Res.CURRENT_LOGIN_ACCOUNT, MODE_PRIVATE).getInt("id", 1))) {
            isLogined = false;
        }
        Cursor cursor = getContentResolver().query(Uri.parse(Res.USER_CONTENT_URL), new String[]{"id", "name", "plan", "dayword"}, "id == ?", new String[]{AppKit.toString(id)}, null);
        if (cursor.moveToNext()) {
            user.setId(cursor.getInt(0));
            user.setName(cursor.getString(1));
            user.setPlan(cursor.getInt(2));
            user.setDayWord(cursor.getInt(3));
            isLogined = true;
        } else {
            user.setId(1);
            user.setName("default");
            user.setPlan(2);
            user.setDayWord(50);
        }
        if (id == 1) {
            isLogined = false;
        }
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        connectivityManager.registerDefaultNetworkCallback(new ConnectivityManager.NetworkCallback() {
            @Override
            public void onAvailable(@NonNull Network network) {
                isNetwork = true;
            }

            @Override
            public void onLosing(@NonNull Network network, int maxMsToLive) {
                isNetwork = false;
            }

            @Override
            public void onLost(@NonNull Network network) {
                isNetwork = false;
                Toast.makeText(OKApplication.this, "网络已断开", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onUnavailable() {
                isNetwork = false;
            }
        });
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }
}
