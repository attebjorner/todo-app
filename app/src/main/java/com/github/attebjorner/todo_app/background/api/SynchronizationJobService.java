package com.github.attebjorner.todo_app.background.api;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.util.Log;

import com.github.attebjorner.todo_app.data.api.ApiRequests;

import java.io.IOException;

public class SynchronizationJobService extends JobService
{
    private static final String TAG = "SyncJobService";

    private boolean jobCancelled = false;

    @Override
    public boolean onStartJob(JobParameters params)
    {
        Log.d(TAG, "onStartJob");
        syncWithServer(params);
        return true;
    }

    private void syncWithServer(JobParameters params)
    {
        ApiRequests apiRequests = new ApiRequests(getApplication());
        new Thread(() ->
        {
            try
            {
                apiRequests.syncTasks();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }).start();
    }

    @Override
    public boolean onStopJob(JobParameters params)
    {
        Log.d(TAG, "onStopJob");
        jobCancelled = true;
        return true;
    }
}
