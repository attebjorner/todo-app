package com.github.attebjorner.todo_app.util;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.github.attebjorner.todo_app.data.dao.NoteDao;
import com.github.attebjorner.todo_app.model.Note;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Note.class}, version = 1, exportSchema = false)
@TypeConverters({RoomConverters.class})
public abstract class NoteRoomDatabase extends RoomDatabase
{
    public static final int NUMBER_OF_THREADS = 4;
    public static final String DATABASE_NAME = "todo_db";
    private static volatile NoteRoomDatabase INSTANCE;
    public static final ExecutorService databaseWriterExecutor
            = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static final RoomDatabase.Callback callback = new RoomDatabase.Callback()
    {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db)
        {
            super.onCreate(db);
            databaseWriterExecutor.execute(() ->
            {
                NoteDao noteDao = INSTANCE.noteDao();
                noteDao.deleteAll();
            });
        }
    };

    public static NoteRoomDatabase getDatabase(final Context context)
    {
        if (INSTANCE == null)
        {
            synchronized (NoteRoomDatabase.class)
            {
                if (INSTANCE == null)
                {
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(), NoteRoomDatabase.class, DATABASE_NAME
                    ).addCallback(callback).build();
                }
            }
        }
        return INSTANCE;
    }

    public abstract NoteDao noteDao();
}
