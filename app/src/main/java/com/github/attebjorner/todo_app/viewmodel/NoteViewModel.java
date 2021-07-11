package com.github.attebjorner.todo_app.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.github.attebjorner.todo_app.data.database.repository.DeletedNoteRepository;
import com.github.attebjorner.todo_app.data.database.repository.NoteRepository;
import com.github.attebjorner.todo_app.model.Note;

import java.util.List;

public class NoteViewModel extends AndroidViewModel
{
    private static NoteRepository noteRepository;

    private static DeletedNoteRepository deletedNoteRepository;

    private final LiveData<List<Note>> notes;

    private final MutableLiveData<Boolean> showDone = new MutableLiveData<>(false);

    private final MutableLiveData<Long> doneCounter = new MutableLiveData<>(0L);

    public NoteViewModel(@NonNull Application application)
    {
        super(application);
        noteRepository = new NoteRepository(application);
        notes = noteRepository.getAllNotes();
        deletedNoteRepository = new DeletedNoteRepository(application);
    }

    public LiveData<List<Note>> getNotes()
    {
        return notes;
    }

    public MutableLiveData<Boolean> getShowDone()
    {
        return showDone;
    }

    public MutableLiveData<Long> getDoneCounter()
    {
        return doneCounter;
    }

    public static void insert(Note note)
    {
        noteRepository.insert(note);
    }

    public LiveData<Note> get(long id)
    {
        return noteRepository.get(id);
    }

    public static void update(Note note)
    {
        note.setDirty(true);
        noteRepository.update(note);
    }

    public static void delete(Note note)
    {
        deletedNoteRepository.insert(note);
        noteRepository.delete(note);
    }
}
