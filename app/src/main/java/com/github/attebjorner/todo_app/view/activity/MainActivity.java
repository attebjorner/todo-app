package com.github.attebjorner.todo_app.view.activity;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.attebjorner.todo_app.R;
import com.github.attebjorner.todo_app.adapter.TodoListAdapter;
import com.github.attebjorner.todo_app.databinding.ActivityMainBinding;
import com.github.attebjorner.todo_app.model.Importance;
import com.github.attebjorner.todo_app.model.Note;
import com.github.attebjorner.todo_app.notification.NotificationJobService;
import com.github.attebjorner.todo_app.viewmodel.NoteViewModel;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class MainActivity extends AppCompatActivity
{
    private ActivityMainBinding binding;
    private NoteViewModel noteViewModel;

    private List<Note> preNotes;
    private List<Note> curNotes;
    private final Set<Note> notesToUpdate = new HashSet<>();
    private final Set<Note> notesToDelete = new HashSet<>();
    private TodoListAdapter adapter;

    private final int[] VISIBLE_R = {R.drawable.ic_visibility, R.drawable.ic_visibility_off};

//    {
//        Note doneLongNote = new Note("Lorem Ipsum - это текст-, часто исполь зуемый в печати и вэб-", null, Importance.NO);
//        doneLongNote.setDone(true);
//        preNotes = Arrays.asList(
//                new Note("2+-", LocalDate.of(2021, 10, 1), Importance.HIGH),
//                new Note("2+-", LocalDate.of(2021, 10, 4), Importance.HIGH),
//                new Note("1+-", LocalDate.of(2021, 10, 2), Importance.LOW),
//                new Note("1+-", LocalDate.of(2021, 10, 5), Importance.LOW),
//                new Note("0+-", LocalDate.of(2021, 10, 3), Importance.NO),
//                new Note("0+-", LocalDate.of(2021, 10, 6), Importance.NO),
//                new Note("2--", null, Importance.HIGH),
//                new Note("1--", null, Importance.LOW),
//                new Note("0--", null, Importance.NO),
//                new Note("2--", null, Importance.HIGH),
//                new Note("1--", null, Importance.LOW),
//                new Note("0--", null, Importance.NO),
//                new Note("Lorem Ipsum - это текст-, часто исполь зуемый в печати и вэб-Lorem Ipsum - это текст-, часто исполь зуемый в печати и вэб-", null, Importance.NO),
//                new Note("nte 3", LocalDate.now(), Importance.NO),
//                doneLongNote,
//                new Note("Lorem Ipsum - это текст-, часто исполь зуемый в печати и вэб-", LocalDate.of(2020, 10, 12), Importance.HIGH)
//        );
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        scheduleNotificationJob();

        noteViewModel = new ViewModelProvider.AndroidViewModelFactory(MainActivity.this.getApplication()).create(NoteViewModel.class);
//        for (Note n : preNotes)
//        {
//            NoteViewModel.insert(n);
//        }
        preconfigRecyclerView();
        setVisibilityAndList();
        setDoneCounter();
        setScrollingAnimation();
        setAddNewBtn();
    }

    public void scheduleNotificationJob()
    {
        ComponentName componentName = new ComponentName(this, NotificationJobService.class);
        JobInfo info = new JobInfo.Builder(123, componentName)
                .setPeriodic(1000 * 60 * 60 * 24)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_NONE)
                .setPersisted(true)
                .build();
        JobScheduler scheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        scheduler.schedule(info);
    }

//    @Override
//    protected void onPause()
//    {
//        super.onPause();
//        for (Note n : notesToUpdate) NoteViewModel.update(n);
//        for (Note n : notesToDelete) NoteViewModel.delete(n);
//        noteViewModel.getDoneCounter().setValue(0L);
//    }

    public void onClickVisibility(View view)
    {
        noteViewModel.getShowDone().setValue(!noteViewModel.getShowDone().getValue());
    }

    public void onClickCreateNote(View view)
    {
        Intent intent = new Intent(this, CreateNoteActivity.class);
        intent.putExtra("isNew", true);
        startActivity(intent);
    }

    private void setVisibilityAndList()
    {
        noteViewModel.getShowDone().observe(this, showDon ->
        {
            noteViewModel.getNotes().observe(MainActivity.this, notes ->
            {
                binding.imbVisible.setImageResource(VISIBLE_R[showDon ? 1 : 0]);
                if (showDon) curNotes = notes;
                else curNotes = notes.stream()
                        .filter(n -> !n.isDone())
                        .collect(Collectors.toList());
                initRecyclerView(curNotes);
                noteViewModel.getDoneCounter().setValue(notes.stream().filter(Note::isDone).count());
            });
        });
    }

    private void setDoneCounter()
    {
        noteViewModel.getDoneCounter().observe(this, aLong ->
                binding.tvDoneCounter.setText(getString(R.string.done, aLong)));
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
        binding.tvNew.setOnClickListener(this::onClickCreateNote);
    }

    private void preconfigRecyclerView()
    {
        LinearLayoutManager llManager = new LinearLayoutManager(
                this, LinearLayoutManager.VERTICAL, false
        );
        binding.rvTodo.setLayoutManager(llManager);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(binding.rvTodo);
    }

    private void initRecyclerView(List<Note> notes)
    {
        adapter = new TodoListAdapter(notes);
        binding.rvTodo.setAdapter(adapter);
        adapter.setCheckboxClickListener((isDone, pos) ->
        {
            if (isDone) setNoteDone(pos);
            else setNoteUndone(pos);
        });
    }

    private void deleteNote(int pos)
    {
        NoteViewModel.delete(curNotes.get(pos));
        adapter.notifyItemRemoved(pos);
    }

    private void setNoteUndone(int pos)
    {
        curNotes.get(pos).setDone(false);
        NoteViewModel.update(curNotes.get(pos));
//        notesToUpdate.remove(curNotes.get(pos));
//        noteViewModel.getDoneCounter().setValue(noteViewModel.getDoneCounter().getValue() - 1);
        adapter.notifyDataSetChanged();
    }

    private void setNoteDone(int pos)
    {
        if (!notesToUpdate.contains(curNotes.get(pos)))
        {
            curNotes.get(pos).setDone(true);
            NoteViewModel.update(curNotes.get(pos));
//            notesToUpdate.add(curNotes.get(pos));
//            noteViewModel.getDoneCounter().setValue(noteViewModel.getDoneCounter().getValue() + 1);
        }
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
