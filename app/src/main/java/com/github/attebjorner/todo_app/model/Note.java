package com.github.attebjorner.todo_app.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.time.LocalDate;
import java.util.Date;
import java.util.Locale;

@Entity(tableName = "notes")
public class Note
{
    @PrimaryKey(autoGenerate = true)
    private long id;

    private String description;

    @ColumnInfo(name = "is_done")
    private boolean isDone;

    private LocalDate deadline;

    private Importance importance;

    public Note(String description, LocalDate deadline, Importance importance)
    {
        this.description = description;
        this.isDone = false;
        this.deadline = deadline;
        this.importance = importance;
    }

    public Note(String description, Importance importance)
    {
        this.description = description;
        this.isDone = false;
        this.importance = importance;
    }

    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public boolean isDone()
    {
        return isDone;
    }

    public void setDone(boolean done)
    {
        isDone = done;
    }

    public LocalDate getDeadline()
    {
        return deadline;
    }

    public void setDeadline(LocalDate deadline)
    {
        this.deadline = deadline;
    }

    public Importance getImportance()
    {
        return importance;
    }

    public void setImportance(Importance importance)
    {
        this.importance = importance;
    }
}
