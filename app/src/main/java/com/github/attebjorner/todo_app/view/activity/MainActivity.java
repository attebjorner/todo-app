package com.github.attebjorner.todo_app.view.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.github.attebjorner.todo_app.R;
import com.github.attebjorner.todo_app.databinding.ActivityMainBinding;
import com.github.attebjorner.todo_app.model.Importance;
import com.github.attebjorner.todo_app.model.Note;
import com.github.attebjorner.todo_app.util.TinyDB;
import com.github.attebjorner.todo_app.view.adapter.TodoListAdapter;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;
import viewmodel.NoteViewModel;

public class MainActivity extends AppCompatActivity
{
    private ActivityMainBinding binding;
    private NoteViewModel noteViewModel;

//    private boolean showDone;
    private List<Note> preNotes;
    private List<Note> curNotes;
//    private long doneNotesCount = 0;
    private TinyDB tinyDB;
    private TodoListAdapter adapter;

    private final int[] VISIBLE_R = {R.drawable.ic_visibility, R.drawable.ic_visibility_off};

    {
        Note doneLongNote = new Note("Lorem Ipsum - это текст-, часто исполь зуемый в печати и вэб-", null, Importance.NO);
        doneLongNote.setDone(true);
        preNotes = Arrays.asList(
                new Note("2+-", LocalDate.of(2021, 10, 1), Importance.HIGH),
                new Note("2+-", LocalDate.of(2021, 10, 4), Importance.HIGH),
                new Note("1+-", LocalDate.of(2021, 10, 2), Importance.LOW),
                new Note("1+-", LocalDate.of(2021, 10, 5), Importance.LOW),
                new Note("0+-", LocalDate.of(2021, 10, 3), Importance.NO),
                new Note("0+-", LocalDate.of(2021, 10, 6), Importance.NO),
                new Note("2--", null, Importance.HIGH),
                new Note("1--", null, Importance.LOW),
                new Note("0--", null, Importance.NO),
                new Note("2--", null, Importance.HIGH),
                new Note("1--", null, Importance.LOW),
                new Note("0--", null, Importance.NO),
                new Note("Lorem Ipsum - это текст-, часто исполь зуемый в печати и вэб-Lorem Ipsum - это текст-, часто исполь зуемый в печати и вэб-", null, Importance.NO),
                new Note("nte 3", LocalDate.now(), Importance.NO),
                doneLongNote,
                new Note("Lorem Ipsum - это текст-, часто исполь зуемый в печати и вэб-", LocalDate.of(2020, 10, 12), Importance.HIGH)
        );
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        noteViewModel = new ViewModelProvider.AndroidViewModelFactory(MainActivity.this.getApplication()).create(NoteViewModel.class);
//        for (Note n : preNotes)
//        {
//            NoteViewModel.insert(n);
//        }

//        noteViewModel.getShowDone().observe(this, showDon ->
//        {
//            if (showDon)
//            {
//                noteViewModel.getNotes().observe(MainActivity.this, notes ->
//                {
//                    initRecyclerView(notes);
//
//                });
//            }
//            else
//            {
//                noteViewModel.getUndoneNotes().observe(MainActivity.this, notes -> initRecyclerView(notes));
//            }
//        });
        noteViewModel.getShowDone().observe(this, showDon ->
        {
            noteViewModel.getNotes().observe(MainActivity.this, notes ->
            {
                if (showDon)
                {
                    binding.imbVisible.setImageResource(VISIBLE_R[1]);
                    curNotes = sortNotesList(notes.stream().filter(n -> !n.isDone()).collect(Collectors.toList()));
                    initRecyclerView(curNotes);
                }
                else
                {
                    binding.imbVisible.setImageResource(VISIBLE_R[0]);
                    curNotes = notes;
                    initRecyclerView(sortNotesList(notes));
                }
                binding.tvDoneCounter.setText(getString(R.string.done, notes.stream().filter(Note::isDone).count()));
            });
        });


        tinyDB = new TinyDB(this);
//        notes = tinyDB.getListObject("notes", Note.class);
//        if (notes.isEmpty())
//        {
//            fillNotes();
//            tinyDB.putListObject("notes", notes);
//        }

//        returns false by default
//        showDone = tinyDB.getBoolean("showDone");

//        binding.imbVisible.setImageResource(VISIBLE_R[noteViewModel.getShowDone().getValue() ? 1 : 0]);
//        if (!showDone)
//        {
//            doneNotesCount = preNotes.stream().filter(Note::isDone).count();
//        }

        setScrollingAnimation();
//        setCounterTv();
//        sortNotesList();
//        initRecyclerView();
        setAddNewBtn();
    }

    public void onClickVisibility(View view)
    {
//        showDone = !showDone;
//        binding.imbVisible.setImageResource(VISIBLE_R[noteViewModel.getShowDone().getValue() ? 1 : 0]);
//        if (!showDone) doneNotesCount = notes.stream().filter(Note::isDone).count();
//        doneNotesCount = 0;
//        tinyDB.putBoolean("showDone", showDone);
        noteViewModel.getShowDone().setValue(!noteViewModel.getShowDone().getValue());
//        initRecyclerView();
    }

    public void onClickCreateNote(View view)
    {
        Intent intent = new Intent(this, CreateNoteActivity.class);
        intent.putExtra("isNew", true);
        startActivity(intent);
    }

    private void setScrollingAnimation()
    {
        binding.tvMyTasks.setOnClickListener(v -> binding.scvTodo.smoothScrollTo(0, 42));
        binding.scvTodo.setOnScrollChangeListener((View.OnScrollChangeListener) (v, scrollX, scrollY,
                                                                            oldScrollX, oldScrollY) ->
        {
            if (scrollY <= 42) binding.motionLayout.transitionToStart();
        });
    }

    private void setAddNewBtn()
    {
        TextView tvNew = (TextView) findViewById(R.id.tvNew);
        tvNew.setOnClickListener(this::onClickCreateNote);
    }

//    private void setCounterTv()
//    {
//        if (!showDone)
//        {
//            binding.tvDoneCounter.setText(getString(R.string.done, noteViewModel.getDoneCount()));
//        }
//    }

    private void initRecyclerView(List<Note> notes)
    {
        LinearLayoutManager llManager = new LinearLayoutManager(
                this, LinearLayoutManager.VERTICAL, false
        );
        binding.rvTodo.setLayoutManager(llManager);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(binding.rvTodo);
//        adapter = new TodoListAdapter(
//                showDone ? preNotes : preNotes.stream()
//                        .filter(x -> !x.isDone())
//                        .collect(Collectors.toList())
//        );
        adapter = new TodoListAdapter(notes);
        binding.rvTodo.setAdapter(adapter);
//        adapter.setOnCheckboxListener(d ->
//        {
//            doneNotesCount += d;
//            binding.tvDoneCounter.setText(getString(R.string.done, doneNotesCount));
//        });
    }

    //    сортирую чтобы первее был дедлайн, потом -- важность
    private List<Note> sortNotesList(List<Note> notes)
    {
        return notes.stream().sorted((o1, o2) ->
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
//        curNotes.remove(pos);
        NoteViewModel.delete(curNotes.get(pos));
        adapter.notifyItemRemoved(pos);
    }

    private void setNoteDone(int pos)
    {
//        preNotes.get(pos).setDone(true);
        curNotes.get(pos).setDone(true);
        NoteViewModel.update(curNotes.get(pos));
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

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView,
                                @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY,
                                int actionState, boolean isCurrentlyActive)
        {
            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addSwipeLeftBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.red))
                    .addSwipeLeftActionIcon(R.drawable.ic_delete_white)
                    .addSwipeRightBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.green))
                    .addSwipeRightActionIcon(R.drawable.ic_check)
                    .create()
                    .decorate();
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };
}
