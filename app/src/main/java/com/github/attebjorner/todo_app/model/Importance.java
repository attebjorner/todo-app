package com.github.attebjorner.todo_app.model;

import java.util.HashMap;
import java.util.Map;

public enum Importance
{
    BASIC(0),
    LOW(1),
    IMPORTANT(2);

    private final int value;

    private static final Map<Integer, Importance> BY_VALUE = new HashMap<>();

    static
    {
        for (Importance i : Importance.values()) BY_VALUE.put(i.value, i);
    }

    Importance(int value)
    {
        this.value = value;
    }

    public int getValue()
    {
        return value;
    }

    static public Importance valueOfInt(int value)
    {
        return BY_VALUE.get(value);
    }
}
