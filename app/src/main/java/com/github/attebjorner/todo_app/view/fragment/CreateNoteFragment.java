package com.github.attebjorner.todo_app.view.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.attebjorner.todo_app.R;
import com.github.attebjorner.todo_app.model.Note;
import com.github.attebjorner.todo_app.util.TinyDB;

public class CreateNoteFragment extends Fragment
{
    private View rootView;
    private Spinner spinner;
    private boolean isNew;

    public CreateNoteFragment(boolean isNew)
    {
        super();
        this.isNew = isNew;
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
        setImportanceSpinner();
        if (!isNew)
        {
            enableDelete();
            setNoteData();
        }
        return rootView;
    }

    private void setImportanceSpinner()
    {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                getContext(), R.array.spinImportance, android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
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
        TinyDB tinyDB = new TinyDB(getContext());
        Note note = tinyDB.getObject("editNote", Note.class);
        TextView tvDescription = rootView.findViewById(R.id.etDescription);
        tvDescription.setText(note.getDescription());
        spinner.setSelection(note.getImportance().getValue());
        if (note.getDeadline() != null)
        {
            TextView tvDate = rootView.findViewById(R.id.tvDate);
            tvDate.setText(note.getDeadline().toString());
        }
    }
}