package com.github.attebjorner.todo_app.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageButton;
import android.widget.TextView;

import com.github.attebjorner.todo_app.R;
import com.github.attebjorner.todo_app.model.Importance;
import com.github.attebjorner.todo_app.model.Note;
import com.github.attebjorner.todo_app.util.TinyDB;
import com.github.attebjorner.todo_app.view.RecyclerViewReadyCallback;
import com.github.attebjorner.todo_app.view.adapter.TodoListAdapter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity
{
    private boolean showDone;
    private List<Note> notes = new ArrayList<>();
    TinyDB tinyDB;
    private final int[] VISIBLE_R = {R.drawable.ic_visibility, R.drawable.ic_visibility_off};

    {
        Note doneLongNote = new Note("Lorem Ipsum - это текст-, часто исполь зуемый в печати и вэб-", Importance.NO);
        doneLongNote.setDone(true);
        notes.addAll(Arrays.asList(
                new Note("2+-", LocalDate.of(2021, 10, 1), Importance.HIGH),
                new Note("2+-", LocalDate.of(2021, 10, 4), Importance.HIGH),
                new Note("1+-", LocalDate.of(2021, 10, 2), Importance.LOW),
                new Note("1+-", LocalDate.of(2021, 10, 5), Importance.LOW),
                new Note("0+-", LocalDate.of(2021, 10, 3), Importance.NO),
                new Note("0+-", LocalDate.of(2021, 10, 6), Importance.NO),
                new Note("2--", Importance.HIGH),
                new Note("1--", Importance.LOW),
                new Note("0--", Importance.NO),
                new Note("2--", Importance.HIGH),
                new Note("1--", Importance.LOW),
                new Note("0--", Importance.NO),
                new Note("Lorem Ipsum - это текст-, часто исполь зуемый в печати и вэб-Lorem Ipsum - это текст-, часто исполь зуемый в печати и вэб-", Importance.NO),
                new Note("nte 3", LocalDate.now(), Importance.NO),
                doneLongNote,
                new Note("Lorem Ipsum - это текст-, часто исполь зуемый в печати и вэб-", LocalDate.of(2021, 10, 12), Importance.HIGH)
        ));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tinyDB = new TinyDB(this);
        tinyDB.putListObject("notes", notes);

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

        sortNotesList();
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

    public void onClickCreateNote(View view)
    {
        Intent intent = new Intent(this, CreateNoteActivity.class);
        startActivity(intent);
    }

    private void initRecyclerView()
    {
        CardView cvTodo = (CardView) findViewById(R.id.cvTodo);
        RecyclerView rvTodo = (RecyclerView) findViewById(R.id.rvTodo);
        LinearLayoutManager llManager = new LinearLayoutManager(
                this, LinearLayoutManager.VERTICAL, false
        );
        rvTodo.setLayoutManager(llManager);
        TodoListAdapter adapter = new TodoListAdapter(
                showDone ? notes : notes.stream()
                        .filter(x -> !x.isDone())
                        .collect(Collectors.toList())
        );
        rvTodo.setAdapter(adapter);

        RecyclerViewReadyCallback recyclerViewReadyCallback;

        recyclerViewReadyCallback = () ->
        {
            rvTodo.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
            int b = rvTodo.getMeasuredWidth();
            cvTodo.getLayoutParams().height = 2000;
            cvTodo.getLayoutParams().width = b;
            cvTodo.requestLayout();
            rvTodo.getLayoutParams().height = 2000;
            rvTodo.getLayoutParams().width = b;
            rvTodo.requestLayout();
        };

        rvTodo.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (recyclerViewReadyCallback != null) {
                    recyclerViewReadyCallback.onLayoutReady();
                }
                rvTodo.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }

//    сортирую чтобы первее был дедлайн, потом -- важность
    private void sortNotesList()
    {
        notes = notes.stream().sorted((o1, o2) ->
        {
            if (o1.getImportance().getValue() < o2.getImportance().getValue()) return 1;
            else if (o1.getImportance().getValue() > o2.getImportance().getValue()) return -1;
            return 0;
        }).sorted((o1, o2) ->
        {
            if (o1.getDeadline() == null || o2.getDeadline() == null)
            {
                if (o1.getDeadline() == null && o2.getDeadline() == null) return 0;
                else if (o1.getDeadline() == null) return 1;
                return -1;
            }
            else if (o1.getDeadline().isBefore(o2.getDeadline())) return -1;
            else if (o1.getDeadline().isAfter(o2.getDeadline())) return 1;
            return 0;
        }).collect(Collectors.toList());
    }
}
