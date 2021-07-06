package com.github.attebjorner.todo_app.model;

import java.util.Map;

public enum Importance
{
    NO(0, "basic"),
    LOW(1, "low"),
    HIGH(2, "important");

    private final int value;

    private final String apiString;

    private static final Map<Integer, Importance> BY_VALUE = Map.of(
            0, NO,
            1, LOW,
            2, HIGH
    );

    private static final Map<String, Importance> BY_API_STRING = Map.of(
            "basic", NO,
            "low", LOW,
            "important", HIGH
    );

    Importance(int value, String apiString)
    {
        this.value = value;
        this.apiString = apiString;
    }

    public int getValue()
    {
        return value;
    }

    public String getApiString()
    {
        return apiString;
    }

    static public Importance valueOfInt(int value)
    {
        return BY_VALUE.get(value);
    }

    static public Importance valueOfApiString(String value)
    {
        return BY_API_STRING.get(value);
    }
}
