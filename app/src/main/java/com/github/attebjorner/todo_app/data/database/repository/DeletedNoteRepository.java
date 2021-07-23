package com.github.attebjorner.todo_app.data.database.repository;

import android.app.Application;

import com.github.attebjorner.todo_app.data.database.NoteRoomDatabase;
import com.github.attebjorner.todo_app.data.database.dao.DeletedNoteDao;
import com.github.attebjorner.todo_app.model.DeletedNote;
import com.github.attebjorner.todo_app.model.Note;

import java.util.List;
import java.util.UUID;

public class DeletedNoteRepository
{
    private final DeletedNoteDao deletedNoteDao;

    public DeletedNoteRepository(Application application)
    {
        NoteRoomDatabase database = NoteRoomDatabase.getDatabase(application);
        this.deletedNoteDao = database.deletedNoteDao();
    }

    public void insert(Note note)
    {
        NoteRoomDatabase.getDatabaseWriterExecutor().execute(
                () -> deletedNoteDao.insertNoteId(new DeletedNote(note.getId().toString()))
        );
    }

    public void deleteAll()
    {
        NoteRoomDatabase.getDatabaseWriterExecutor().execute(deletedNoteDao::deleteAll);
    }

    public List<String> getDeletedNotesIds()
    {
        return deletedNoteDao.getDeletedNotesIds();
    }
}
