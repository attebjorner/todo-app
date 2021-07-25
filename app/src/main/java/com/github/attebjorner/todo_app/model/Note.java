package com.github.attebjorner.todo_app.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity(tableName = "notes")
public class Note
{
    @PrimaryKey
    @NonNull
    private UUID id;

    private String description;

    @ColumnInfo(name = "is_done")
    private boolean isDone;

    private LocalDate deadline;

    private Importance importance;

    @ColumnInfo(name = "creation_date")
    private LocalDateTime creationDate;

    @ColumnInfo(name = "last_update")
    private LocalDateTime lastUpdate;

    @ColumnInfo(name = "is_dirty")
    private boolean isDirty;

    public Note(String description, LocalDate deadline, Importance importance)
    {
        this.id = UUID.randomUUID();
        this.description = description;
        this.isDone = false;
        this.deadline = deadline;
        this.importance = importance;
        creationDate = LocalDateTime.now();
        lastUpdate = LocalDateTime.now();
        this.isDirty = true;
    }

    @NonNull
    public UUID getId()
    {
        return id;
    }

    public void setId(@NonNull UUID id)
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

    public LocalDateTime getCreationDate()
    {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate)
    {
        this.creationDate = creationDate;
    }

    public LocalDateTime getLastUpdate()
    {
        return lastUpdate;
    }

    public void setLastUpdate(LocalDateTime lastUpdate)
    {
        this.lastUpdate = lastUpdate;
    }

    public boolean isDirty()
    {
        return isDirty;
    }

    public void setDirty(boolean dirty)
    {
        isDirty = dirty;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (o == null || getClass() != o.getClass())
        {
            return false;
        }
        Note note = (Note) o;
        return isDone == note.isDone &&
                isDirty == note.isDirty &&
                id.equals(note.id) &&
                Objects.equals(description, note.description) &&
                Objects.equals(deadline, note.deadline) &&
                importance == note.importance &&
                Objects.equals(creationDate, note.creationDate) &&
                Objects.equals(lastUpdate, note.lastUpdate);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(id, description, isDone, deadline, importance, creationDate, lastUpdate, isDirty);
    }
}
