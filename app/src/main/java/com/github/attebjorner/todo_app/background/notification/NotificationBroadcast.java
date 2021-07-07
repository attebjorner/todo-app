package com.github.attebjorner.todo_app.background.notification;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.github.attebjorner.todo_app.R;
import com.github.attebjorner.todo_app.view.activity.MainActivity;

public class NotificationBroadcast extends BroadcastReceiver
{
    private static final String TAG = "ReminderBroadcast";

    @Override
    public void onReceive(Context context, Intent intent)
    {
        Log.d(TAG, "broadcast start");
        Intent mainIntent = new Intent(context, MainActivity.class);
        PendingIntent mainPendingIntent = PendingIntent.getActivity(context, 1, mainIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "todoChannel")
                .setContentTitle("Todo")
                .setContentText(String.format("You have %d tasks to do", intent.getIntExtra("todoCount", 0)))
                .setSmallIcon(R.drawable.ic_check)
                .setContentIntent(mainPendingIntent)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setCategory(NotificationCompat.CATEGORY_REMINDER);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(123, builder.build());
    }
}
