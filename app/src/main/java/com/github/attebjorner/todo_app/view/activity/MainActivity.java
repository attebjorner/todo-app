package com.github.attebjorner.todo_app.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.github.attebjorner.todo_app.R;
import com.github.attebjorner.todo_app.model.Importance;
import com.github.attebjorner.todo_app.model.Note;
import com.github.attebjorner.todo_app.util.TinyDB;
import com.github.attebjorner.todo_app.view.adapter.TodoListAdapter;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity
{
    private boolean showDone;
    private List<Note> notes;
    TinyDB tinyDB;
    private final int[] VISIBLE_R = {R.drawable.ic_visibility, R.drawable.ic_visibility_off};

    {
        Note doneLongNote = new Note("Lorem Ipsum - это текст-, часто исполь зуемый в печати и вэб-", Importance.NO);
        doneLongNote.setDone(true);
        notes = Arrays.asList(
                new Note("note 1", Importance.LOW),
                new Note("nte 3", LocalDate.now(), Importance.NO),
                doneLongNote,
                new Note("Lorem Ipsum - это текст-, часто исполь зуемый в печати и вэб-", LocalDate.now(), Importance.HIGH)
        );
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tinyDB = new TinyDB(this);
        try
        {
            showDone = tinyDB.getBoolean("showDone");
        }
        catch (Exception e)
        {
            showDone = false;
            tinyDB.putBoolean("showDone", false);
        }

        TextView tvDone = (TextView) findViewById(R.id.tvDoneCounter);
        tvDone.setText(getString(R.string.done, notes.stream().filter(Note::isDone).count()));

        initRecyclerView();
    }

    public void onClickVisibility(View view)
    {
        ImageButton imbVisible = (ImageButton) view;
        imbVisible.setImageResource(VISIBLE_R[showDone ? 0 : 1]);
        showDone = !showDone;
        tinyDB.putBoolean("showDone", showDone);
        initRecyclerView();
    }

    private void initRecyclerView()
    {
        RecyclerView rvTodo = (RecyclerView) findViewById(R.id.rvTodo);
        LinearLayoutManager llManager = new LinearLayoutManager(
                this, LinearLayoutManager.VERTICAL, false
        );
        rvTodo.setLayoutManager(llManager);
        rvTodo.setAdapter(new TodoListAdapter(
                showDone ? notes : notes.stream()
                        .filter(x -> !x.isDone())
                        .collect(Collectors.toList())
        ));
    }

    private void sortNotesList()
    {
        notes = notes.stream().sorted((o1, o2) ->
        {
            if (o1.getImportance().getValue() < o2.getImportance().getValue()) return 1;
            else if (o1.getImportance().getValue() > o2.getImportance().getValue()) return -1;
            return 0;
        }).sorted((o1, o2) ->
        {
            if (o1.getDeadline().isBefore(o2.getDeadline())) return 1;
            else if (o1.getDeadline().isAfter(o2.getDeadline())) return -1;
            return 0;
        }).collect(Collectors.toList());
    }
}
