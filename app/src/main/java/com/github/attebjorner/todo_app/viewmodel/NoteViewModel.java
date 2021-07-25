package com.github.attebjorner.todo_app.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.github.attebjorner.todo_app.data.database.repository.DeletedNoteRepository;
import com.github.attebjorner.todo_app.data.database.repository.NoteRepository;
import com.github.attebjorner.todo_app.model.Note;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class NoteViewModel extends ViewModel
{
    private static NoteRepository noteRepository;

    private static DeletedNoteRepository deletedNoteRepository;

    private final LiveData<List<Note>> notes;

    private final MutableLiveData<Boolean> showDone = new MutableLiveData<>(false);

    private final MutableLiveData<Long> doneCounter = new MutableLiveData<>(0L);

    @Inject
    public NoteViewModel(NoteRepository noteRepo, DeletedNoteRepository deletedNoteRepo)
    {
        noteRepository = noteRepo;
        deletedNoteRepository = deletedNoteRepo;
        notes = noteRepository.getAllNotes();
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
