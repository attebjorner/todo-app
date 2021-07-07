package com.github.attebjorner.todo_app.view;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Build;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.github.attebjorner.todo_app.data.api.ApiRequests;
import com.github.attebjorner.todo_app.util.TinyDB;

import java.util.Observable;

public class App extends Application
{
    @Override
    public void onCreate()
    {
        super.onCreate();
        createNotificationChannel();

        ApiRequests apiRequests = new ApiRequests(this);
        TinyDB tinyDB = new TinyDB(this);
        if (!tinyDB.getBoolean("not_first_time_lauched"))
        {
            apiRequests.fillDbInitTasks();
            tinyDB.putBoolean("not_first_time_lauched", true);
        }
    }

    private void createNotificationChannel()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            CharSequence name = "reminderChannel";
            String description = "channel for reminder";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("todoChannel", name, importance);
            channel.setDescription(description);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }
}
