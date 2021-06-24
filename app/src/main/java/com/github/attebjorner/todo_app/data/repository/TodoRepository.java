package com.github.attebjorner.todo_app.data.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.github.attebjorner.todo_app.data.dao.NoteDao;
import com.github.attebjorner.todo_app.model.Note;
import com.github.attebjorner.todo_app.util.NoteRoomDatabase;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class TodoRepository
{
    private final NoteDao noteDao;
    private final LiveData<List<Note>> notes;

    public TodoRepository(Application application)
    {
        NoteRoomDatabase database = NoteRoomDatabase.getDatabase(application);
        this.noteDao = database.noteDao();
        this.notes = noteDao.getNotes();
    }

    public LiveData<List<Note>> getAllNotes()
    {
        return notes;
    }

    public LiveData<List<Note>> getUndoneNotes()
    {
        return noteDao.getUndoneNotes();
    }

    public void insert(Note note)
    {
        NoteRoomDatabase.databaseWriterExecutor.execute(() -> noteDao.insertNote(note));
    }

    public LiveData<Note> get(long id)
    {
        return noteDao.get(id);
    }

    public void update(Note note)
    {
        NoteRoomDatabase.databaseWriterExecutor.execute(() -> noteDao.update(note));
    }

    public void delete(Note note)
    {
        NoteRoomDatabase.databaseWriterExecutor.execute(() -> noteDao.delete(note));
    }

//    public int getDoneCount() throws ExecutionException, InterruptedException
//    {
//        final Future<Integer> submit = NoteRoomDatabase.databaseWriterExecutor.submit(() -> noteDao.getDoneNotesCount());
//        return submit.get();
//    }
}
