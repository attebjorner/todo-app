package com.github.attebjorner.todo_app.view.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.github.attebjorner.todo_app.R;

public class CreateNoteActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);

        setImportanceSpinner();
    }

    private void setImportanceSpinner()
    {
        Spinner spinner = (Spinner) findViewById(R.id.spinImportance);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.spinImportance, android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    public void onClickBack(View view)
    {
        onBackPressed();
    }
}