package com.github.attebjorner.todo_app.notification;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.util.Log;

import com.github.attebjorner.todo_app.data.repository.TodoRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;

public class NotificationJobService extends JobService
{
    private static final String TAG = "NotificationJobService";
    private boolean jobCancelled = false;

    @Override
    public boolean onStartJob(JobParameters params)
    {
        Log.d(TAG, "job started");
        doBackgroundWord(params);
        return true;
    }

    private void doBackgroundWord(JobParameters params)
    {
        TodoRepository repository = new TodoRepository(getApplication());
        new Thread(() ->
        {
            if (jobCancelled) return;
            LocalDate day;
            if (LocalTime.now().getHour() >= 10) day = LocalDate.now().plusDays(1);
            else day = LocalDate.now();
            int count = repository.getUndoneNotesByDate(day.toEpochDay());
            if (count == 0) return;
            createNotificationAlarm(count, day);
        }).start();
    }

    private void createNotificationAlarm(int count, LocalDate day)
    {
        Intent reminderIntent = new Intent(getApplicationContext(), NotificationBroadcast.class);
        reminderIntent.putExtra("todoCount", count);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                getApplicationContext(), 0, reminderIntent, 0
        );
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        LocalDateTime date = LocalDateTime.of(day, LocalTime.of(10, 0, 0));
        Log.d(TAG, "calendar is set");
        alarmManager.set(
                AlarmManager.RTC_WAKEUP,
                date.atZone(ZoneId.systemDefault()).toEpochSecond() * 1000,
                pendingIntent
        );
    }

    @Override
    public boolean onStopJob(JobParameters params)
    {
        jobCancelled = true;
        return true;
    }
}
