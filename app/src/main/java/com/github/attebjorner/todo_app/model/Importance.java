package com.github.attebjorner.todo_app.model;

public enum Importance
{
    NO("Нет"), LOW("Низкий"), HIGH("!! Высокий");

    private String value;

    private Importance(String value)
    {
        this.value = value;
    }

    public String getValue()
    {
        return value;
    }
}
