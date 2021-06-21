package com.github.attebjorner.todo_app.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.github.attebjorner.todo_app.R;
import com.github.attebjorner.todo_app.model.Note;
import com.github.attebjorner.todo_app.util.TinyDB;
import com.github.attebjorner.todo_app.view.fragment.CreateNoteFragment;

import java.util.List;

public class CreateNoteActivity extends AppCompatActivity
{
    private Intent intent;
    boolean isNew;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);

        intent = getIntent();
        isNew = intent.getBooleanExtra("isNew", true);
        setFragment();
    }

    public void onClickBack(View view)
    {
        onBackPressed();
    }

    private void setFragment()
    {
        Fragment fragment = (isNew)
                ? new CreateNoteFragment(true)
                : new CreateNoteFragment(false, intent.getIntExtra("pos", -1));
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.mainFragment, fragment);
        ft.commit();
    }
}