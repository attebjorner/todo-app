package com.github.attebjorner.todo_app.view.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.github.attebjorner.todo_app.R;

public class MainActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}