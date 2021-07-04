package com.github.attebjorner.todo_app.data.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.github.attebjorner.todo_app.data.dao.NoteDao;
import com.github.attebjorner.todo_app.model.Note;
import com.github.attebjorner.todo_app.data.NoteRoomDatabase;

import java.time.LocalDateTime;
import java.util.List;

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

    public void insert(Note note)
    {
        NoteRoomDatabase.getDatabaseWriterExecutor().execute(() -> noteDao.insertNote(note));
    }

    public LiveData<Note> get(long id)
    {
        return noteDao.get(id);
    }

    public void update(Note note)
    {
        note.setLastUpdate(LocalDateTime.now());
        NoteRoomDatabase.getDatabaseWriterExecutor().execute(() -> noteDao.update(note));
    }

    public void delete(Note note)
    {
        NoteRoomDatabase.getDatabaseWriterExecutor().execute(() -> noteDao.delete(note));
    }

    public int getUndoneNotesByDate(long epochday)
    {
        return noteDao.getUndoneNotesByDate(epochday);
    }
}
