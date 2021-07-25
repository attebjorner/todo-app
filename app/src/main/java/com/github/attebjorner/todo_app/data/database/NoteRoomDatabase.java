package com.github.attebjorner.todo_app.data.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.github.attebjorner.todo_app.data.database.dao.DeletedNoteDao;
import com.github.attebjorner.todo_app.data.database.dao.NoteDao;
import com.github.attebjorner.todo_app.model.DeletedNote;
import com.github.attebjorner.todo_app.model.Note;
import com.github.attebjorner.todo_app.util.RoomConverters;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Note.class, DeletedNote.class}, version = 1, exportSchema = false)
@TypeConverters({RoomConverters.class})
public abstract class NoteRoomDatabase extends RoomDatabase
{
    private static final int NUMBER_OF_THREADS = 4;

    private static final ExecutorService databaseWriterExecutor
            = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public abstract NoteDao noteDao();

    public abstract DeletedNoteDao deletedNoteDao();

    public static ExecutorService getDatabaseWriterExecutor()
    {
        return databaseWriterExecutor;
    }
}
