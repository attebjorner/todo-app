package com.github.attebjorner.todo_app.view;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Build;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.github.attebjorner.todo_app.background.sync.SyncJobService;
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
        scheduleSyncJob();
    }

    private void scheduleSyncJob()
    {
        ComponentName componentName = new ComponentName(this, SyncJobService.class);
        JobInfo info = new JobInfo.Builder(1234, componentName)
                .setPeriodic(1000 * 60 * 60 * 8)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                .setPersisted(true)
                .build();
        JobScheduler scheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        scheduler.schedule(info);
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
