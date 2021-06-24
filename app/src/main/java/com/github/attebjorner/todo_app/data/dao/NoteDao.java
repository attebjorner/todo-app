package com.github.attebjorner.todo_app.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.github.attebjorner.todo_app.model.Note;

import java.util.List;

@Dao
public interface NoteDao
{
    @Insert
    void insertNote(Note note);

    @Query("DELETE FROM notes")
    void deleteAll();

    @Query("SELECT * FROM notes")
    LiveData<List<Note>> getNotes();

    @Query("SELECT * FROM notes WHERE notes.id == :id")
    LiveData<Note> get(long id);

    @Update
    void update(Note note);

    @Delete
    void delete(Note note);
}
