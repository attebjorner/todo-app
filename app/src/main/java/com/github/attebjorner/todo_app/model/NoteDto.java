package com.github.attebjorner.todo_app.model;

import com.github.attebjorner.todo_app.util.RoomConverters;
import com.google.gson.annotations.SerializedName;

import java.util.UUID;

public class NoteDto
{
    private String id;

    private String text;

    private String importance;

    private boolean done;

    private long deadline;

    @SerializedName("created_at")
    private long createdTime;

    @SerializedName("updated_at")
    private long updatedTime;

    private NoteDto(String id, String text, String importance, boolean done,
                   long deadline, long createdTime, long updatedTime)
    {
        this.id = id;
        this.text = text;
        this.importance = importance;
        this.done = done;
        this.deadline = deadline;
        this.createdTime = createdTime;
        this.updatedTime = updatedTime;
    }

    public Note toNote()
    {
        Note note = new Note(
                text, RoomConverters.toLocalDate(deadline),
                Importance.valueOfApiString(importance)
        );
        note.setId(UUID.fromString(id));
        note.setDone(done);
        note.setCreationDate(RoomConverters.toLocalDateTime(createdTime));
        note.setLastUpdate(RoomConverters.toLocalDateTime(updatedTime));
        return note;
    }

    public static NoteDto fromNote(Note note)
    {
        return new NoteDto(
                note.getId().toString(), note.getDescription(),
                note.getImportance().getApiString(), note.isDone(),
                RoomConverters.fromLocalDate(note.getDeadline()),
                RoomConverters.fromLocalDateTime(note.getCreationDate()),
                RoomConverters.fromLocalDateTime(note.getLastUpdate())
        );
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getText()
    {
        return text;
    }

    public void setText(String text)
    {
        this.text = text;
    }

    public String getImportance()
    {
        return importance;
    }

    public void setImportance(String importance)
    {
        this.importance = importance;
    }

    public boolean isDone()
    {
        return done;
    }

    public void setDone(boolean done)
    {
        this.done = done;
    }

    public long getDeadline()
    {
        return deadline;
    }

    public void setDeadline(long deadline)
    {
        this.deadline = deadline;
    }

    public long getCreatedTime()
    {
        return createdTime;
    }

    public void setCreatedTime(long createdTime)
    {
        this.createdTime = createdTime;
    }

    public long getUpdatedTime()
    {
        return updatedTime;
    }

    public void setUpdatedTime(long updatedTime)
    {
        this.updatedTime = updatedTime;
    }
}
