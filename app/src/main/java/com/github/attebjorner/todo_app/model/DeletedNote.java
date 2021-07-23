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
    private String noteId;

    public DeletedNote(String noteId)
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

    public String getNoteId()
    {
        return noteId;
    }

    public void setNoteId(String noteId)
    {
        this.noteId = noteId;
    }
}
