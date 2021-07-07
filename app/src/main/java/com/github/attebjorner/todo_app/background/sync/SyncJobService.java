package com.github.attebjorner.todo_app.background.sync;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.os.Build;
import android.util.Log;

import com.github.attebjorner.todo_app.data.api.ApiRequests;

import java.io.IOException;

public class SyncJobService extends JobService
{
    private static final String TAG = "SyncJobService";

    private boolean jobCancelled = false;

    @Override
    public boolean onStartJob(JobParameters params)
    {
        Log.d(TAG, "onStartJob");
        ConnectivityManager.NetworkCallback networkCallback = new ConnectivityManager.NetworkCallback()
        {
            @Override
            public void onAvailable(Network network)
            {
                Log.d(TAG, "onAvailable");
                syncWithServer(params);
            }
        };
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
        {
            connectivityManager.registerDefaultNetworkCallback(networkCallback);
        }
        else
        {
            NetworkRequest request = new NetworkRequest.Builder()
                    .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET).build();
            connectivityManager.registerNetworkCallback(request, networkCallback);
        }
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
