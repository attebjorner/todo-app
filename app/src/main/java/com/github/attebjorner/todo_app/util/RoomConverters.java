package com.github.attebjorner.todo_app.util;

import androidx.room.TypeConverter;

import com.github.attebjorner.todo_app.model.Importance;

import java.time.LocalDate;

public class RoomConverters
{
    @TypeConverter
    public static Long fromLocalDate(LocalDate date)
    {
        return date == null ? null : date.toEpochDay();
    }

    @TypeConverter
    public static LocalDate toLocalDate(Long value)
    {
        return value == null ? null : LocalDate.ofEpochDay(value);
    }

    @TypeConverter
    public static String fromImportance(Importance importance)
    {
        return importance == null ? null : importance.name();
    }

    @TypeConverter
    public static Importance toImportance(String value)
    {
        return value == null ? null : Importance.valueOf(value);
    }
}
