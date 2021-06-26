package com.github.attebjorner.todo_app.notification;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Intent;

import androidx.annotation.Nullable;

import com.github.attebjorner.todo_app.data.repository.TodoRepository;
import com.github.attebjorner.todo_app.view.activity.MainActivity;

import java.time.LocalDate;
import java.util.Calendar;

public class ReminderService extends IntentService
{
    public ReminderService()
    {
        super("reminder service");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent)
    {
        TodoRepository repository = new TodoRepository(getApplication());
        int count = repository.getUndoneNotesByDate(LocalDate.now().toEpochDay());
        if (count > 0)
        {
            Intent reminderIntent = new Intent(getApplicationContext(), ReminderBroadcast.class);
            reminderIntent.putExtra("todoCount", count);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(
                    getApplicationContext(), 0, reminderIntent, 0
            );
            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.set(Calendar.HOUR_OF_DAY, 10);
            alarmManager.setInexactRepeating(
                    AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY, pendingIntent
            );
        }
    }
}
