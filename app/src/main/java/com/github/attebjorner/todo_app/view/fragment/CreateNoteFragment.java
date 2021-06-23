package com.github.attebjorner.todo_app.view.fragment;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.attebjorner.todo_app.R;
import com.github.attebjorner.todo_app.model.Importance;
import com.github.attebjorner.todo_app.model.Note;
import com.github.attebjorner.todo_app.util.TinyDB;
import com.github.attebjorner.todo_app.view.activity.CreateNoteActivity;
import com.github.attebjorner.todo_app.view.activity.MainActivity;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.List;

public class CreateNoteFragment extends Fragment
{
    private View rootView;
    private Spinner spinner;
    private boolean isNew;
    private TinyDB tinyDB;
    private List<Note> notes;
    private int pos = -1;
    private LocalDate date;

    private TextView tvDescription;
    private TextView tvDate;

    public CreateNoteFragment(boolean isNew)
    {
        super();
        this.isNew = isNew;
    }

    public CreateNoteFragment(boolean isNew, int pos)
    {
        super();
        this.isNew = isNew;
        this.pos = pos;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        rootView = inflater.inflate(R.layout.fragment_create_note, container, false);

        spinner = (Spinner) rootView.findViewById(R.id.spinImportance);
        tvDescription = rootView.findViewById(R.id.etDescription);
        tvDate = rootView.findViewById(R.id.tvDate);
        tinyDB = new TinyDB(getContext());
        notes = tinyDB.getListObject("notes", Note.class);
        setImportanceSpinner();
        if (!isNew)
        {
            enableDelete();
            setNoteData();
        }

        SwitchCompat switchDate = rootView.findViewById(R.id.switchDate);
        Calendar newCalendar = Calendar.getInstance();
        DatePickerDialog datePicker = new DatePickerDialog(getContext(), R.style.DateDialog,
                (view, year, monthOfYear, dayOfMonth) ->
        {
            date = LocalDate.of(year, monthOfYear, dayOfMonth);
            tvDate.setText(date.toString());
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH),
                newCalendar.get(Calendar.DAY_OF_MONTH)
        );

        switchDate.setOnClickListener(v ->
        {
            if (date == null) datePicker.show();
            else
            {
                date = null;
                tvDate.setText("");
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
                tvDescription.getText().toString(),
                (tvDate.getText().length() == 0) ? null : date,
                Importance.values()[(int) spinner.getSelectedItemId()]
        );
        notes.add(note);
        tinyDB.putListObject("notes", notes);
    }

    public void onSaveNote(View view)
    {
        notes.get(pos).setDescription(tvDescription.getText().toString());
        notes.get(pos).setDeadline((tvDate.getText().length() == 0) ? null : date);
        notes.get(pos).setImportance(Importance.values()[(int) spinner.getSelectedItemId()]);
        tinyDB.putListObject("notes", notes);
    }

    private void setImportanceSpinner()
    {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                getContext(), R.array.spinImportance, android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(0);
    }

    private void enableDelete()
    {
        TextView tvDelete = rootView.findViewById(R.id.tvDelete);
        tvDelete.setTextColor(0xFFFF3B30);
        ImageButton imbDelete = rootView.findViewById(R.id.imbDelete);
        imbDelete.setBackgroundResource(R.drawable.ic_delete);
    }

    private void setNoteData()
    {
        tinyDB = new TinyDB(getContext());
        Note note = tinyDB.getObject("editNote", Note.class);
        tvDescription.setText(note.getDescription());
        spinner.setSelection(note.getImportance().getValue());
        if (note.getDeadline() != null)
        {
            tvDate.setText(note.getDeadline().toString());
        }
    }
}