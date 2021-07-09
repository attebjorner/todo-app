package com.github.attebjorner.todo_app.view.fragment;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;

import com.github.attebjorner.todo_app.R;
import com.github.attebjorner.todo_app.databinding.FragmentCreateNoteBinding;
import com.github.attebjorner.todo_app.model.Importance;
import com.github.attebjorner.todo_app.model.Note;
import com.github.attebjorner.todo_app.util.TinyDB;
import com.github.attebjorner.todo_app.view.activity.MainActivity;

import java.time.LocalDate;
import java.util.Calendar;

import com.github.attebjorner.todo_app.viewmodel.NoteViewModel;

public class CreateNoteFragment extends Fragment
{
    private boolean isNew;

    private Note note;

    private LocalDate date;

    private DatePickerDialog datePicker;

    private FragmentCreateNoteBinding binding;

    private final Spanned[] SPINNER_ITEMS = new Spanned[3];

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        binding = FragmentCreateNoteBinding.inflate(inflater, container, false);
        View rootView = binding.getRoot();

        fillSpinnerItems();

        TinyDB tinyDB = new TinyDB(getContext());
        isNew = tinyDB.getBoolean("isNewFragment");

        if (!isNew)
        {
            note = tinyDB.getObject("editNote", Note.class);
            enableDelete();
            setNoteData();
        }

        setImportanceSpinner();
        setDatePickerDialog();
        setSwitchDate();
        setSaveButton();

        return rootView;
    }

    public void onCreateNew(View view)
    {
        Note note = new Note(
                binding.etDescription.getText().toString(),
                (binding.tvDate.getText().length() == 0) ? null : date,
                Importance.values()[(int) binding.spinImportance.getSelectedItemId()]
        );
        NoteViewModel.insert(note);
    }

    public void onSaveNote(View view)
    {
        note.setDescription(binding.etDescription.getText().toString());
        note.setDeadline(
                binding.tvDate.getText().length() == 0
                        ? null
                        : LocalDate.parse(binding.tvDate.getText())
        );
        note.setImportance(Importance.values()[(int) binding.spinImportance.getSelectedItemId()]);
        NoteViewModel.update(note);
    }

    private void fillSpinnerItems()
    {
        SPINNER_ITEMS[0] = Html.fromHtml(
                "<font color=" + getString(R.string.label_primary_color) + ">"
                        + getString(R.string.importance_no) + "</font> "
        );
        SPINNER_ITEMS[1] = Html.fromHtml(
                "<font color=" + getString(R.string.label_primary_color) + ">"
                        + getString(R.string.importance_low) + "</font> "
        );
        SPINNER_ITEMS[2] = Html.fromHtml(
                "<font color=" + getString(R.string.red_color) + ">"
                        + getString(R.string.importance_high) + "</font> "
        );
    }

    private void setDatePickerDialog()
    {
        Calendar newCalendar = Calendar.getInstance();
        datePicker = new DatePickerDialog(getContext(), R.style.DateDialog,
                (view, year, monthOfYear, dayOfMonth) ->
                {
                    date = LocalDate.of(year, monthOfYear + 1, dayOfMonth);
                    binding.tvDate.setText(date.toString());
                }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH),
                newCalendar.get(Calendar.DAY_OF_MONTH)
        );
    }

    private void setSwitchDate()
    {
        binding.switchDate.setOnCheckedChangeListener((buttonView, isChecked) ->
        {
            if (isChecked) datePicker.show();
            else
            {
                date = null;
                binding.tvDate.setText("");
            }
        });
    }

    private void setSaveButton()
    {
        Button btnSave = (Button) getActivity().findViewById(R.id.btnSave);
        btnSave.setOnClickListener(v ->
        {
            if (isNew) onCreateNew(v);
            else onSaveNote(v);
            Intent intent = new Intent(v.getContext(), MainActivity.class);
            v.getContext().startActivity(intent);
        });
    }

    private void setImportanceSpinner()
    {

        ArrayAdapter<Spanned> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, SPINNER_ITEMS);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinImportance.setAdapter(adapter);
        binding.spinImportance.setSelection(0);
    }

    private void enableDelete()
    {
        binding.tvDelete.setTextColor(ContextCompat.getColor(getContext(), R.color.red));
        binding.imbDelete.setBackgroundResource(R.drawable.ic_delete);

        View.OnClickListener deleteListener = v ->
        {
            NoteViewModel.delete(note);
            Intent intent = new Intent(v.getContext(), MainActivity.class);
            v.getContext().startActivity(intent);
        };

        binding.imbDelete.setOnClickListener(deleteListener);
        binding.tvDelete.setOnClickListener(deleteListener);
    }

    private void setNoteData()
    {
        binding.etDescription.setText(note.getDescription());
        binding.spinImportance.setSelection(note.getImportance().getValue());
        if (note.getDeadline() != null)
        {
            binding.tvDate.setText(note.getDeadline().toString());
            binding.switchDate.setChecked(true);
        }
    }
}