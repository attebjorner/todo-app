package com.github.attebjorner.todo_app.model;

public enum Importance
{
    NO(0),
    LOW(1),
    HIGH(2);

    private final int value;

    Importance(int value)
    {
        this.value = value;
    }

    public int getValue()
    {
        return value;
    }
}
