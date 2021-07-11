package com.github.attebjorner.todo_app.util;

import androidx.room.TypeConverter;

import com.github.attebjorner.todo_app.model.Importance;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.TimeZone;
import java.util.UUID;

public class RoomConverters
{
    @TypeConverter
    public static String fromUuid(UUID id)
    {
        return id.toString();
    }

    @TypeConverter
    public static UUID toUuid(String value)
    {
        return UUID.fromString(value);
    }

    @TypeConverter
    public static Long fromLocalDate(LocalDate date)
    {
        return date == null ? null : date.atStartOfDay().toEpochSecond(ZoneOffset.UTC);
    }

    @TypeConverter
    public static LocalDate toLocalDate(Long value)
    {
        return value == null ? null : LocalDateTime.ofEpochSecond(
                value, 0, ZoneOffset.UTC
        ).toLocalDate();
    }

    @TypeConverter
    public static Long fromLocalDateTime(LocalDateTime date)
    {
        return date == null ? null : date.atZone(ZoneId.systemDefault()).toEpochSecond();
    }

    @TypeConverter
    public static LocalDateTime toLocalDateTime(Long value)
    {
        return value == null ? null : LocalDateTime.ofEpochSecond(
            value, 0, ZoneOffset.UTC
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
