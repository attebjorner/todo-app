package com.github.attebjorner.todo_app.model;

import java.util.Date;

public class Note
{
    private String title;

    private String description;

    private boolean isDone;

    private Date deadline;

    private Importance importance;

    public Note(String title, String description, boolean isDone, Date deadline, Importance importance)
    {
        this.title = title;
        this.description = description;
        this.isDone = isDone;
        this.deadline = deadline;
        this.importance = importance;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
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
