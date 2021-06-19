package com.github.attebjorner.todo_app.model;

import java.util.Date;

public class Note
{
    private long id;

    private String description;

    private boolean isDone;

    private Date deadline;

    private Importance importance;

    public Note(String description, Date deadline, Importance importance)
    {
        this.description = description;
        this.isDone = false;
        this.deadline = deadline;
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

    public Date getDeadline()
    {
        return deadline;
    }

    public void setDeadline(Date deadline)
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
