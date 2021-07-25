package com.github.attebjorner.todo_app.data.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.github.attebjorner.todo_app.model.DeletedNote;

import java.util.List;
import java.util.UUID;

@Dao
public interface DeletedNoteDao
{
    @Insert
    void insertNoteId(DeletedNote id);

    @Query("DELETE FROM deleted_notes")
    void deleteAll();

    @Query("SELECT deleted_notes.note_id FROM deleted_notes")
    List<String> getDeletedNotesIds();
}
