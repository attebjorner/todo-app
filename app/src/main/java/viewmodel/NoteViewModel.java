package viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.github.attebjorner.todo_app.data.repository.TodoRepository;
import com.github.attebjorner.todo_app.model.Note;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class NoteViewModel extends AndroidViewModel
{
    public static TodoRepository repository;
    public final LiveData<List<Note>> notes;
    public MutableLiveData<Boolean> showDone = new MutableLiveData<>(false);

    public NoteViewModel(@NonNull Application application)
    {
        super(application);
        repository = new TodoRepository(application);
        notes = repository.getAllNotes();
    }

    public LiveData<List<Note>> getNotes()
    {
        return notes;
    }

    public MutableLiveData<Boolean> getShowDone()
    {
        return showDone;
    }

    public static void insert(Note note)
    {
        repository.insert(note);
    }

    public LiveData<Note> get(long id)
    {
        return repository.get(id);
    }

    public static void update(Note note)
    {
        repository.update(note);
    }

    public static void delete(Note note)
    {
        repository.delete(note);
    }
}
