package com.github.attebjorner.todo_app.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.github.attebjorner.todo_app.R;
import com.github.attebjorner.todo_app.util.TinyDB;
import com.github.attebjorner.todo_app.view.fragment.CreateNoteFragment;

public class CreateNoteActivity extends AppCompatActivity
{
    private Intent intent;

    private boolean isNew;

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
        TinyDB tinyDB = new TinyDB(this);
        tinyDB.putBoolean("isNewFragment", isNew);
        if (!isNew) tinyDB.putLong("noteId", intent.getLongExtra("id", -1));
        Fragment fragment = new CreateNoteFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.mainFragment, fragment);
        ft.commit();
    }
}