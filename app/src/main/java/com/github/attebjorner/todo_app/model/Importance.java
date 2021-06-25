package com.github.attebjorner.todo_app.model;

import java.util.Map;

public enum Importance
{
    NO(0),
    LOW(1),
    HIGH(2);

    private final int value;

    private static final Map<Integer, Importance> BY_VALUE = Map.of(
            0, NO,
            1, LOW,
            2, HIGH
    );

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
