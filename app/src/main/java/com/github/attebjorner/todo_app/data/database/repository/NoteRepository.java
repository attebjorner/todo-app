package com.github.attebjorner.todo_app.data.database.repository;

import androidx.lifecycle.LiveData;

import com.github.attebjorner.todo_app.data.database.NoteRoomDatabase;
import com.github.attebjorner.todo_app.data.database.dao.NoteDao;
import com.github.attebjorner.todo_app.model.Note;

import java.time.LocalDateTime;
import java.util.List;

import javax.inject.Inject;

public class NoteRepository
{
    private final NoteDao noteDao;

    private final LiveData<List<Note>> notes;

    @Inject
    public NoteRepository(NoteDao noteDao)
    {
        this.noteDao = noteDao;
        this.notes = noteDao.getNotes();
    }

    public LiveData<List<Note>> getAllNotes()
    {
        return notes;
    }

    public List<Note> getDirtyNotes()
    {
        return noteDao.getDirtyNotes();
    }

    public List<Note> getUndirtyNotes()
    {
        return noteDao.getUndirtyNotes();
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
