package com.github.attebjorner.todo_app.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.UUID;

@Entity(tableName = "deleted_notes")
public class DeletedNote
{
    @PrimaryKey(autoGenerate = true)
    private long id;

    @ColumnInfo(name = "note_id")
    private UUID noteId;

    public DeletedNote(UUID noteId)
    {
        this.noteId = noteId;
    }

    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public UUID getNoteId()
    {
        return noteId;
    }

    public void setNoteId(UUID noteId)
    {
        this.noteId = noteId;
    }
}
