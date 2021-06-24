package com.github.attebjorner.todo_app.view.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity
{
    private boolean showDone;
    private List<Note> notes = new ArrayList<>();
    private long doneNotesCount = 0;
    private TextView tvDone;
    private TinyDB tinyDB;
    private ImageButton imbVisible;
    private TodoListAdapter adapter;
    private final int[] VISIBLE_R = {R.drawable.ic_visibility, R.drawable.ic_visibility_off};

    private void fillNotes()
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
                new Note("Lorem Ipsum - это текст-, часто исполь зуемый в печати и вэб-", LocalDate.of(2020, 10, 12), Importance.HIGH)
        ));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tinyDB = new TinyDB(this);

        notes = tinyDB.getListObject("notes", Note.class);
        if (notes.isEmpty())
        {
            fillNotes();
            tinyDB.putListObject("notes", notes);
        }
//        returns false by default
        showDone = tinyDB.getBoolean("showDone");

        imbVisible = (ImageButton) findViewById(R.id.imbVisible);
        imbVisible.setImageResource(VISIBLE_R[showDone ? 1 : 0]);
        if (!showDone)
        {
            doneNotesCount = notes.stream().filter(Note::isDone).count();
        }
        setCounterTv();
        sortNotesList();
        initRecyclerView();

        TextView tvNew = (TextView) findViewById(R.id.tvNew);
        tvNew.setOnClickListener(this::onClickCreateNote);
    }

    public void onClickVisibility(View view)
    {
        ImageButton imbVisible = (ImageButton) view;
        showDone = !showDone;
        imbVisible.setImageResource(VISIBLE_R[showDone ? 1 : 0]);
//        if (!showDone) doneNotesCount = notes.stream().filter(Note::isDone).count();
        doneNotesCount = 0;
        tinyDB.putBoolean("showDone", showDone);
        initRecyclerView();
    }

    public void onClickCreateNote(View view)
    {
        Intent intent = new Intent(this, CreateNoteActivity.class);
        intent.putExtra("isNew", true);
        startActivity(intent);
    }

    private void setCounterTv()
    {
        tvDone = (TextView) findViewById(R.id.tvDoneCounter);
        if (!showDone)
        {
            tvDone.setText(getString(R.string.done, doneNotesCount));
        }
    }

    private void initRecyclerView()
    {
        RecyclerView rvTodo = (RecyclerView) findViewById(R.id.rvTodo);
        LinearLayoutManager llManager = new LinearLayoutManager(
                this, LinearLayoutManager.VERTICAL, false
        );
        rvTodo.setLayoutManager(llManager);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(rvTodo);
        adapter = new TodoListAdapter(
                showDone ? notes : notes.stream()
                        .filter(x -> !x.isDone())
                        .collect(Collectors.toList())
        );
        rvTodo.setAdapter(adapter);
        adapter.setOnCheckboxListener(d ->
        {
            doneNotesCount += d;
            tvDone.setText(getString(R.string.done, doneNotesCount));
        });
    }

    //    сортирую чтобы первее был дедлайн, потом -- важность
    private void sortNotesList()
    {
        notes = notes.stream().sorted((o1, o2) ->
        {
            if (o1.getImportance().getValue() < o2.getImportance().getValue())
            {
                return 1;
            }
            else if (o1.getImportance().getValue() > o2.getImportance().getValue())
            {
                return -1;
            }
            return 0;
        }).sorted((o1, o2) ->
        {
            if (o1.getDeadline() == null || o2.getDeadline() == null)
            {
                if (o1.getDeadline() == null && o2.getDeadline() == null)
                {
                    return 0;
                }
                else if (o1.getDeadline() == null)
                {
                    return 1;
                }
                return -1;
            }
            else if (o1.getDeadline().isBefore(o2.getDeadline()))
            {
                return -1;
            }
            else if (o1.getDeadline().isAfter(o2.getDeadline()))
            {
                return 1;
            }
            return 0;
        }).collect(Collectors.toList());
    }

    private void deleteNote(int pos)
    {
        notes.remove(pos);
        adapter.notifyItemRemoved(pos);
    }

    private void setNoteDone(int pos)
    {
        notes.get(pos).setDone(true);
        adapter.notifyDataSetChanged();
    }

    ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(
            0, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT
    )
    {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView,
                              @NonNull RecyclerView.ViewHolder viewHolder,
                              @NonNull RecyclerView.ViewHolder target)
        {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction)
        {
            switch (direction)
            {
                case ItemTouchHelper.RIGHT:
                    setNoteDone(viewHolder.getAdapterPosition());
                    break;
                case ItemTouchHelper.LEFT:
                    deleteNote(viewHolder.getAdapterPosition());
                    break;
            }
        }
    };
}
