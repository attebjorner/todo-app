package com.github.attebjorner.todo_app.data.dao;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
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

    @Query("SELECT * FROM notes ORDER BY notes.is_done, notes.deadline IS NULL, notes.deadline, notes.importance DESC")
    LiveData<List<Note>> getNotes();

    @Query("SELECT * FROM notes WHERE notes.id == :id")
    LiveData<Note> get(long id);

    @Update
    void update(Note note);

    @Delete
    void delete(Note note);

    @Query("SELECT COUNT(notes.id) FROM notes WHERE notes.is_done == 0 AND notes.deadline == :epochDay")
    int getUndoneNotesByDate(long epochDay);
}
