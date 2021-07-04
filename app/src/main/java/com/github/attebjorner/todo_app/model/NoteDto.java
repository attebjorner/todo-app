package com.github.attebjorner.todo_app.model;

import com.google.gson.annotations.SerializedName;

public class NoteDto
{
    private String id;

    private String text;

    private String priority;

    private boolean done;

    private long deadline;

    @SerializedName("created_at")
    private long createdTime;

    @SerializedName("updated_at")
    private long updatedTime;

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

    public String getPriority()
    {
        return priority;
    }

    public void setPriority(String priority)
    {
        this.priority = priority;
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
