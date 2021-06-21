package com.github.attebjorner.todo_app.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;

import com.github.attebjorner.todo_app.R;
import com.github.attebjorner.todo_app.view.fragment.CreateNoteFragment;

public class CreateNoteActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);

        setFragment();
    }

    public void onClickBack(View view)
    {
        onBackPressed();
    }

    private void setFragment()
    {
        Fragment fragment = new CreateNoteFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.mainFragment, fragment);
        ft.commit();
    }
}