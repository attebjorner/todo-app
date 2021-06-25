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
    public static Integer fromImportance(Importance importance)
    {
        return importance == null ? null : importance.getValue();
    }

    @TypeConverter
    public static Importance toImportance(Integer value)
    {
        return value == null ? null : Importance.valueOfInt(value);
    }
}
