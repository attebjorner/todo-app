package com.github.attebjorner.todo_app.di;

import android.app.Application;

import androidx.room.Room;

import com.github.attebjorner.todo_app.data.database.NoteRoomDatabase;
import com.github.attebjorner.todo_app.data.database.dao.DeletedNoteDao;
import com.github.attebjorner.todo_app.data.database.dao.NoteDao;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class DatabaseModule
{
    private static final String DATABASE_NAME = "todo_db";

    @Singleton
    @Provides
    public static NoteRoomDatabase providePokemonDB(Application application)
    {
        return Room.databaseBuilder(application, NoteRoomDatabase.class, DATABASE_NAME).build();
    }

    @Singleton
    @Provides
    public static NoteDao provideNoteDao(NoteRoomDatabase database)
    {
        return database.noteDao();
    }

    @Singleton
    @Provides
    public static DeletedNoteDao provideDeletedNoteDao(NoteRoomDatabase database)
    {
        return database.deletedNoteDao();
    }
}
