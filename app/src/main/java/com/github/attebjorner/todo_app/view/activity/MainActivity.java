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
import com.github.attebjorner.todo_app.background.sync.SyncJobService;
import com.github.attebjorner.todo_app.view.adapter.TodoListAdapter;
import com.github.attebjorner.todo_app.databinding.ActivityMainBinding;
import com.github.attebjorner.todo_app.model.Note;
import com.github.attebjorner.todo_app.background.notification.NotificationJobService;
import com.github.attebjorner.todo_app.viewmodel.NoteViewModel;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class MainActivity extends AppCompatActivity
{
    private ActivityMainBinding binding;

    private NoteViewModel noteViewModel;

    private List<Note> curNotes;

    private final Map<UUID, Note> notesToUpdate = new HashMap<>();

    private final Set<Note> notesToDelete = new HashSet<>();

    private TodoListAdapter adapter;

    private final int[] VISIBLE_R = {R.drawable.ic_visibility, R.drawable.ic_visibility_off};

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        scheduleNotificationJob();
        scheduleSynchronizationJob();

        noteViewModel = new ViewModelProvider.AndroidViewModelFactory(this.getApplication())
                .create(NoteViewModel.class);

        preconfigRecyclerView();
        observeVisibilityAndList();
        setDoneCounter();
        setScrollingAnimation();
        setAddNewBtn();
    }

    private void scheduleNotificationJob()
    {
        ComponentName componentName = new ComponentName(this, NotificationJobService.class);
        JobInfo info = new JobInfo.Builder(123, componentName)
                .setPeriodic(1000 * 60 * 60 * 8)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_NONE)
                .setPersisted(true)
                .build();
        JobScheduler scheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        scheduler.schedule(info);
    }

    private void scheduleSynchronizationJob()
    {
        ComponentName componentName = new ComponentName(this, SyncJobService.class);
        JobInfo info = new JobInfo.Builder(1234, componentName)
                .setPeriodic(1000 * 60 * 60 * 8)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                .setPersisted(true)
                .build();
        JobScheduler scheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        scheduler.schedule(info);
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        updateDatabaseData();
    }

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

    private void observeVisibilityAndList()
    {
        noteViewModel.getShowDone().observe(this, showDon ->
        {
            updateDatabaseData();
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

    private void updateDatabaseData()
    {
        for (Note n : notesToUpdate.values()) NoteViewModel.update(n);
        for (Note n : notesToDelete) NoteViewModel.delete(n);
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
        adapter.setCheckboxClickListener((isDone, pos) -> changeNoteState(pos, isDone));
    }

    private void deleteNote(int pos)
    {
        if (curNotes.get(pos).isDone()) noteViewModel.getDoneCounter().setValue(noteViewModel.getDoneCounter().getValue() - 1);
        notesToDelete.add(curNotes.get(pos));
        curNotes.remove(pos);
        adapter.notifyItemRemoved(pos);
    }

    private void changeNoteState(int pos, boolean isDone)
    {
        if (curNotes.get(pos).isDone() == isDone)
        {
            curNotes.get(pos).setDone(!isDone);
            notesToUpdate.put(curNotes.get(pos).getId(), curNotes.get(pos));
            noteViewModel.getDoneCounter().setValue(
                    noteViewModel.getDoneCounter().getValue() + (!isDone ? 1 : -1)
            );
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
                    changeNoteState(viewHolder.getAdapterPosition(), false);
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
