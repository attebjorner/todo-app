package com.github.attebjorner.todo_app.view.fragment;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.SwitchCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.attebjorner.todo_app.R;
import com.github.attebjorner.todo_app.databinding.FragmentCreateNoteBinding;
import com.github.attebjorner.todo_app.model.Importance;
import com.github.attebjorner.todo_app.model.Note;
import com.github.attebjorner.todo_app.util.TinyDB;
import com.github.attebjorner.todo_app.view.activity.CreateNoteActivity;
import com.github.attebjorner.todo_app.view.activity.MainActivity;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.List;

import viewmodel.NoteViewModel;

public class CreateNoteFragment extends Fragment
{
    private boolean isNew;
    private TinyDB tinyDB;
//    private List<Note> notes;
//    private int pos = -1;
    private long noteId;
    private LocalDate date;
    private NoteViewModel noteViewModel;

    private FragmentCreateNoteBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        binding = FragmentCreateNoteBinding.inflate(inflater, container, false);
        View rootView = binding.getRoot();

        noteViewModel = new ViewModelProvider.AndroidViewModelFactory(getActivity().getApplication()).create(NoteViewModel.class);

        tinyDB = new TinyDB(getContext());

        isNew = tinyDB.getBoolean("isNewFragment");
        if (!isNew) noteId = tinyDB.getLong("noteId");
//        if (!isNew) pos = tinyDB.getInt("posFragment");

//        notes = tinyDB.getListObject("notes", Note.class);
        setImportanceSpinner();
        if (!isNew)
        {
            enableDelete();
            setNoteData();
        }

        Calendar newCalendar = Calendar.getInstance();
        DatePickerDialog datePicker = new DatePickerDialog(getContext(), R.style.DateDialog,
                (view, year, monthOfYear, dayOfMonth) ->
        {
            date = LocalDate.of(year, monthOfYear + 1, dayOfMonth);
            binding.tvDate.setText(date.toString());
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH),
                newCalendar.get(Calendar.DAY_OF_MONTH)
        );

        binding.switchDate.setOnCheckedChangeListener((buttonView, isChecked) ->
        {
            if (isChecked) datePicker.show();
            else
            {
                date = null;
                binding.tvDate.setText("");
            }
        });

        Button btnSave = (Button) getActivity().findViewById(R.id.btnSave);
        btnSave.setOnClickListener(v ->
        {
            if (isNew) onCreateNew(v);
            else onSaveNote(v);
            Intent intent = new Intent(v.getContext(), MainActivity.class);
            v.getContext().startActivity(intent);
        });

        return rootView;
    }

    public void onCreateNew(View view)
    {
        Note note = new Note(
                binding.etDescription.getText().toString(),
                (binding.tvDate.getText().length() == 0) ? null : date,
                Importance.values()[(int) binding.spinImportance.getSelectedItemId()]
        );
//        notes.add(note);
        NoteViewModel.insert(note);
//        tinyDB.putListObject("notes", notes);
    }

    public void onSaveNote(View view)
    {
//        Note note = noteViewModel.get(noteId);
        final Note[] tnote = new Note[1];
        noteViewModel.get(noteId).observe(getViewLifecycleOwner(), new Observer<Note>()
        {
            @Override
            public void onChanged(Note note)
            {
//                tnote[0] = note;
                note.setDescription(binding.etDescription.getText().toString());
                note.setDeadline((binding.tvDate.getText().length() == 0) ? null : date);
                note.setImportance(Importance.values()[(int) binding.spinImportance.getSelectedItemId()]);
                NoteViewModel.update(note);
            }
        });
//        note.setDescription(binding.etDescription.getText().toString());
//        note.setDeadline((binding.tvDate.getText().length() == 0) ? null : date);
//        note.setImportance(Importance.values()[(int) binding.spinImportance.getSelectedItemId()]);
//        NoteViewModel.update(note);
//        tinyDB.putListObject("notes", notes);
    }

    private void setImportanceSpinner()
    {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                getContext(), R.array.spinImportance, android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinImportance.setAdapter(adapter);
        binding.spinImportance.setSelection(0);
    }

    private void enableDelete()
    {
        binding.tvDelete.setTextColor(ContextCompat.getColor(getContext(), R.color.red));
        binding.imbDelete.setBackgroundResource(R.drawable.ic_delete);
    }

    private void setNoteData()
    {
        tinyDB = new TinyDB(getContext());
//        Note note = tinyDB.getObject("editNote", Note.class);
//        Note n = noteViewModel.get(noteId);
//        Note note = noteViewModel.get(noteId);
        noteViewModel.get(noteId).observe(getViewLifecycleOwner(), note ->
        {
            binding.etDescription.setText(note.getDescription());
            binding.spinImportance.setSelection(note.getImportance().getValue());
            if (note.getDeadline() != null)
            {
                binding.tvDate.setText(note.getDeadline().toString());
                binding.switchDate.setChecked(true);
            }
        });
//        binding.etDescription.setText(note.getDescription());
//        binding.spinImportance.setSelection(note.getImportance().getValue());
//        if (note.getDeadline() != null)
//        {
//            binding.tvDate.setText(note.getDeadline().toString());
//            binding.switchDate.setChecked(true);
//        }
    }
}