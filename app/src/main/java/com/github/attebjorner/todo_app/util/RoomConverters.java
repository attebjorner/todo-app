package com.github.attebjorner.todo_app.util;

import androidx.room.TypeConverter;

import com.github.attebjorner.todo_app.model.Importance;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.TimeZone;

public class RoomConverters
{
    @TypeConverter
    public static Long fromLocalDate(LocalDate date)
    {
        return date == null ? null : date.atStartOfDay().toEpochSecond(ZoneOffset.UTC) * 1000;
    }

    @TypeConverter
    public static LocalDate toLocalDate(Long value)
    {
        return value == null ? null : LocalDateTime.ofEpochSecond(
                value / 1000, 0, ZoneOffset.UTC
        ).toLocalDate();
    }

    @TypeConverter
    public static Long fromLocalDateTime(LocalDateTime date)
    {
        return date == null ? null : date.atZone(ZoneId.systemDefault()).toEpochSecond() * 1000;
    }

    @TypeConverter
    public static LocalDateTime toLocalDateTime(Long value)
    {
        return value == null ? null : LocalDateTime.ofInstant(
                Instant.ofEpochMilli(value), ZoneId.systemDefault()
        );
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
