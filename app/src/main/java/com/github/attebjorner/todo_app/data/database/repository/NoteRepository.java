package com.github.attebjorner.todo_app.data.database.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.github.attebjorner.todo_app.data.database.dao.NoteDao;
import com.github.attebjorner.todo_app.data.database.NoteRoomDatabase;
import com.github.attebjorner.todo_app.model.Note;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class NoteRepository
{
    private final NoteDao noteDao;

    private final LiveData<List<Note>> notes;

    public NoteRepository(Application application)
    {
        NoteRoomDatabase database = NoteRoomDatabase.getDatabase(application);
        this.noteDao = database.noteDao();
        this.notes = noteDao.getNotes();
    }

    public LiveData<List<Note>> getAllNotes()
    {
        return notes;
    }

    public Set<Note> getSetDirtyNotes()
    {
        return new HashSet<>(noteDao.getDirtyNotes());
    }

    public Set<Note> getSetUndirtyNotes()
    {
        return new HashSet<>(noteDao.getUndirtyNotes());
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
