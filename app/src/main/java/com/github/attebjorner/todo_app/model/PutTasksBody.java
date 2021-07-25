package com.github.attebjorner.todo_app.model;

import java.util.List;

public class PutTasksBody
{
    private List<String> deleted;

    private List<NoteDto> other;

    public PutTasksBody(List<String> deleted, List<NoteDto> other)
    {
        this.deleted = deleted;
        this.other = other;
    }

    public List<String> getDeleted()
    {
        return deleted;
    }

    public void setDeleted(List<String> deleted)
    {
        this.deleted = deleted;
    }

    public List<NoteDto> getOther()
    {
        return other;
    }

    public void setOther(List<NoteDto> other)
    {
        this.other = other;
    }
}
