package com.github.attebjorner.todo_app.data.database.repository;

import com.github.attebjorner.todo_app.data.database.NoteRoomDatabase;
import com.github.attebjorner.todo_app.data.database.dao.DeletedNoteDao;
import com.github.attebjorner.todo_app.model.DeletedNote;
import com.github.attebjorner.todo_app.model.Note;

import java.util.List;

import javax.inject.Inject;

public class DeletedNoteRepository
{
    private final DeletedNoteDao deletedNoteDao;

    @Inject
    public DeletedNoteRepository(DeletedNoteDao deletedNoteDao)
    {
        this.deletedNoteDao = deletedNoteDao;
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
